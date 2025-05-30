package techno_express.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import techno_express.backend.interceptor.RequestIdInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RequestIdInterceptor requestIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestIdInterceptor)
                .addPathPatterns("/**") // detecta todas las rutas
                .excludePathPatterns("/error"); // pero excluime esta
    }
}