/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author phumu
 */
public class ChatApplicationTest {

    @Test
    public void testUsernameValid() {
        assertTrue(ChatApplication.checkUserName("usr_1"));
    }

    @Test
    public void testUsernameInvalid_NoUnderscore() {
        assertFalse(ChatApplication.checkUserName("user1"));
    }

    @Test
    public void testPasswordValid() {
        assertTrue(ChatApplication.checkPasswordComplexity("Pass@123"));
    }

    @Test
    public void testPasswordInvalid_NoSpecialChar() {
        assertFalse(ChatApplication.checkPasswordComplexity("Password123"));
    }

    @Test
    public void testValidPhone() {
        assertTrue(ChatApplication.checkCellPhoneNumber("+27821234567"));
    }

    @Test
    public void testInvalidPhone() {
        assertFalse(ChatApplication.checkCellPhoneNumber("082123456"));
    }
}