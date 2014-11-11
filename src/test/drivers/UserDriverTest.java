package test.drivers;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import drivers.UserDriver;


public class UserDriverTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsCredentialsValid() {
		String email="test@test.com";
		String password_hash="gwRqWu588Ocd6otgdTBhQw==";
		Tables.UserTable user= UserDriver.getByEmail(email);
		Assert.assertEquals("Credentials Valid"	, password_hash, user.getPasswordHash());	
	}

	@Test
	public void testGetById() {
		int id = 15;
		Tables.UserTable  user = UserDriver.getById(id);
		Assert.assertEquals("Got user by id"	, id, user.getId());
	}

	@Test
	public void testGetByEmail() {
		String email = "test@test.com";
		Tables.UserTable  user = UserDriver.getByEmail(email);
		Assert.assertEquals("Email"	, email, user.getEmail());
	}
	/*@Test
	public void testCreateUser() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testGetUsersByTag() {
		int tagId=1;
		Tables.UserTable [] users=UserDriver.getUsersByTag(tagId);
		Assert.assertEquals(12, users[0].getId());
	}

}
