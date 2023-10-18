package com.raphaelfsg.blogpessoal.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;


//Indicando que a classe é um componente
//OncePerRequestFilter garante que essa classe seja chamada apenas uma vez para cada execução
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    //Esse método vai receber uma requisição, uma resposta e a interface de filtragem
    //Dentro passamos 3 strings para receber o header da requisição de login, o token e o nome de usuário
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        /* Verifica se o cabeçalho é nulo. Caso não seja e comece com "Bearer " ele vai remover os 7 primeiros caracteres
        O que equivale a exatamente a palavra iniciada e depois vai extrair o username do token */
        try {
            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                username = jwtService.extractUsername(token);
            }

            /* Essa segunda verificação tenta confirmar se o username não é nulo e se o contexto de autenticação é nulo.
            Caso o teste passe, ele vai carregar os detalhes do usuário através do nome de usuário digitado */
            if (username!= null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                /* Verifica se o token é válido e, caso positivo, ele gera o token e o devolve dentro do filtro */
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }   catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | ResponseStatusException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
    }
}
