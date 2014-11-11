package test.drivers;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Tables.PostsTable;

import drivers.PostDriver;

public class PostDriverTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetWallPosts() {
		int user_id=15;
		PostsTable[] posts= PostDriver.getWallPosts(user_id);
		Assert.assertEquals("There is a wall post",1, posts[0].getId());
			
	}

	@Test
	public void testGetPostsByUserId() {
		int user_id=16;
		PostsTable[] posts= PostDriver.getPostsByUserId(user_id);
		Assert.assertEquals("There is a wall post",user_id, posts[0].getOwnerId());
	}

	@Test
	public void testGetPostsByTag() {
		int tagId=1;
		PostsTable[] posts= PostDriver.getPostsByTag(tagId);
		Assert.assertEquals("There is a wall post",1 , posts[0].getId());
	}

	/*@Test
	public void testAddPosts() {
		int user_id=10;
		String post="PostAdded";
		Assert.assertTrue("Post is added", PostDriver.addPosts(user_id, post));
		
	}*/

}
