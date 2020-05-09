package com.github.chrisgleissner.microservice.springboot.security.auth.user;

// Spring requires "ROLE_" prefix for all roles, e.g. "ROLE_ADMIN" means hasRole("ADMIN") passes
public interface SpringRoleNameUtil {
    String ROLE_PREFIX = "ROLE_";

    static String fromSpringRoleName(String roleName) {
        if (roleName.startsWith(ROLE_PREFIX))
            return roleName.substring(ROLE_PREFIX.length());
        return roleName;
    }

    static String toSpringRoleName(String roleName) {
        return ROLE_PREFIX + roleName;
    }
}
