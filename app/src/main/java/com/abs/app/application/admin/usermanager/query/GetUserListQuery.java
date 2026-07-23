package com.abs.app.application.admin.usermanager.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserListQuery {
    private int page;
    private int limit;
    private String role;
    private String status;
    private String search;
}