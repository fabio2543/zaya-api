package com.zaya.api.domain.user.service.policy;
import org.springframework.stereotype.Component;
import com.zaya.api.domain.user.model.User;
import com.zaya.api.domain.user.model.UserRole;


@Component
public class AdminFirstLoginPolicy  {

    public boolean canApplyFullUpdate(User user) {
        
        return user.getRole() == UserRole.ADMIN && user.getDateUpdate() == null;
        
        }

    }
