package test.drivers;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Tables.TagsTable;
import Tables.UserTable;

import drivers.TagsDriver;

public class TagsDriverTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateTagsArray() {
		String tags= "cat,dog,bird";
		TagsTable [] tagsArr=TagsDriver.createTagsArray(tags);
		Assert.assertEquals("cat", tagsArr[0].getTag());
	}

	@Test
	public void testGetById() {
		int id=1;
		TagsTable tags= TagsDriver.getById(id);
		Assert.assertEquals("yemek", tags.getTag());
	}

	@Test
	public void testGetByUserId() {
		int userId=12;
		TagsTable [] tags= TagsDriver.getByUserId(userId);
		Assert.assertEquals(1, (int)tags[0].getId());
	}

	/*@Test
	public void testCreateTags() throws Exception {
		TagsTable [] tagsTable= TagsDriver.getByUserId(12);
		UserTable userTable= UserDriver.getById(12);
		Assert.assertEquals(false, TagsDriver.createTags(tagsTable, userTable));
	}*/

	@Test
	public void testTagId() {
		String tag="radiohead";
		int id= TagsDriver.tagId(tag);
		Assert.assertEquals(4, id);
		
	}

	/*@Test
	public void testInsertTags() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertTagsInUsers() {
		fail("Not yet implemented");
	}*/

	@Test
	public void testGetUsers() {
		int firstId=12;
		int secondId=16;
		UserTable[] tags= TagsDriver.getUsers(1);
		Assert.assertEquals(firstId, tags[0].getId());
		Assert.assertEquals(secondId, tags[1].getId());
		
	}

	/*@Test
	public void testInsertTagsInPosts() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsertUserInPost() {
		fail("Not yet implemented");
	}*/

}
