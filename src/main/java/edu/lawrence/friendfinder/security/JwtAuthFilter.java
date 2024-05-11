package edu.lawrence.friendfinder.security;

// In-project includes [Services]
import edu.lawrence.friendfinder.services.JwtService;

// Jakarta-level includes [Exceptions]
import jakarta.servlet.ServletException;

// Jakarta-level includes [Filter Helpers]
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Java-level includes [Exceptions]
import java.io.IOException;

// Spring-level includes [Class Annotations]
import org.springframework.stereotype.Component;

// Spring-level includes [Interfaces]
import org.springframework.web.filter.OncePerRequestFilter;

// Spring-level includes [Field Annotations]
import org.springframework.beans.factory.annotation.Autowired;

// Spring-level includes [Security Helpers]
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userid = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			if (jwtService.isValid(token))
				userid = jwtService.getSubject(token);
		}

		if (userid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			RestaurantUserDetails userDetails = new RestaurantUserDetails(userid);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		}

		filterChain.doFilter(request, response);
	}
	
}