package com.focusGureumWebApp.focusGureumWebdemo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
/* It's used to carry data during login
*/

public class AuthRequest {

    private String nickname;
    private String password;

}