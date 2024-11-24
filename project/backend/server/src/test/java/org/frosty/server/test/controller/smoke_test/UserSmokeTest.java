// UserSmokeTest.java
package org.frosty.server.test.controller.smoke_test;

import lombok.extern.slf4j.Slf4j;
import org.frosty.server.controller.user.AuthController;
import org.frosty.server.entity.bo.User;
import org.frosty.server.entity.po.UserPublicInfo;
import org.frosty.server.test.controller.auth.AuthAPI;
import org.frosty.server.test.controller.user.UserAPI;
import org.frosty.test_common.annotation.IdempotentControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@IdempotentControllerTest
public class UserSmokeTest {




    @Autowired
    private UserAPI userAPI;

    @Autowired
    private AuthAPI authAPI;

    @Test
    public void testUserCRUD() throws Exception {
        // Create user
        User newUser = new User()
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setRole(User.Role.student)
                .setEmail("john.doe@test.com");
        userAPI.registerUserSuccess(newUser, "password123");

        // Login to get token
        var loginInfo = new AuthController.LoginInfo(newUser.getEmail(), "password123");
        var token = authAPI.loginSuccess(loginInfo).getToken();

        var searchResult = userAPI.searchByNameSuccess(token, "John", "Doe", 1, 10);
        assertEquals(1, searchResult.size());
        var searchUser = searchResult.get(0);
        newUser.setUserId(searchUser.getUserId());

        // Get user
        User createdUser = userAPI.getUserSuccess(token, newUser.getUserId());
        assertNotNull(createdUser);
        assertEquals("John", createdUser.getFirstName());

        // Update user
        createdUser.setFirstName("Jane");
        userAPI.updateUserSuccess(token, createdUser.getUserId(), createdUser);

        // Get updated user
        User updatedUser = userAPI.getUserSuccess(token, createdUser.getUserId());
        assertEquals("Jane", updatedUser.getFirstName());

        // Delete user
        userAPI.deleteUserSuccess(token, createdUser.getUserId());

        // Verify user deletion
        User deletedUser = userAPI.getUserSuccess(token, createdUser.getUserId());
        assertNull(deletedUser);
    }

    @Test
    public void testUserPublicInfo() throws Exception {
        // Create user
        User newUser = new User()
                .setFirstName("Alice")
                .setLastName("Smith")
                .setPassword("password123")
                .setRole(User.Role.teacher)
                .setEmail("alice.smith@test.com");
        userAPI.registerUserSuccess(newUser, "password123");

        // Login to get token
        var loginInfo = new AuthController.LoginInfo(newUser.getEmail(), "password123");
        var token = authAPI.loginSuccess(loginInfo).getToken();

        var searchResult = userAPI.searchByNameSuccess(token, "Alice", "Smith", 1, 10);
        assertEquals(1, searchResult.size());
        searchResult = userAPI.searchByNameSuccess(token, "Alice", "Smith", 100, -1);
        assertEquals(1, searchResult.size());
        newUser.setUserId(searchResult.get(0).getUserId());

        // Get public user info
        UserPublicInfo publicInfo = userAPI.getUserPublicInfoSuccess(token, newUser.getUserId());
        assertNotNull(publicInfo);
        assertEquals("Alice", publicInfo.getFirstName());
        assertEquals("Smith", publicInfo.getLastName());
        assertEquals(User.Role.teacher, publicInfo.getRole());
        assertEquals("alice.smith@test.com", publicInfo.getEmail());
    }
}