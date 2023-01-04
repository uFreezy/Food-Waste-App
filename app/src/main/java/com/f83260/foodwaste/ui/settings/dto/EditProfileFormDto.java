package com.f83260.foodwaste.ui.settings.dto;

import com.f83260.foodwaste.databinding.SettingsActivityBinding;
import com.f83260.foodwaste.ui.common.dto.UserProfileFormDto;

public class EditProfileFormDto extends UserProfileFormDto {

    public EditProfileFormDto(SettingsActivityBinding binding){
        super(binding.profileForm.firstName, binding.profileForm.lastName, binding.profileForm.phone, binding.profileForm.username, binding.profileForm.password);
    }

}
