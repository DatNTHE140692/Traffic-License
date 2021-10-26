package vn.edu.fpt.traffic_license.model.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.fpt.traffic_license.entities.Role;
import vn.edu.fpt.traffic_license.entities.User;
import vn.edu.fpt.traffic_license.entities.UserRole;
import vn.edu.fpt.traffic_license.repository.RoleRepository;
import vn.edu.fpt.traffic_license.repository.UserRoleRepository;
import vn.edu.fpt.traffic_license.utils.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private User user;
    private UserRoleRepository userRoleRepository;
    private RoleRepository roleRepository;

    public UserDetailsImpl(User user, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.user = user;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = Collections.emptySet();
        Set<UserRole> userRoles = userRoleRepository.findByUserId(user.getId());
        if (Utils.isNotEmpty(userRoles)) {
            roles = roleRepository.findByIdIn(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
        }
        return Utils.isEmpty(roles) ? Collections.emptySet() : roles.stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
