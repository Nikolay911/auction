//package com.auction1.security;

/*    @Component
    public class AuthenticationEntryPointImpl extends BasicAuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
                throws IOException {
            response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.println("HTTP Status 401 - " + authEx.getMessage());
        }

        @Override
        public void afterPropertiesSet() {
            // RealmName appears in the login window (Firefox).
            setRealmName("o7planning");
            super.afterPropertiesSet();
        }

    }*/

