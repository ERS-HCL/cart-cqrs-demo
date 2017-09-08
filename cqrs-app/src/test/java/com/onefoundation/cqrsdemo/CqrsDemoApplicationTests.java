package com.onefoundation.cqrsdemo;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CqrsDemoApplicationTests {
	
	@Mock 
	MyList myList;
	
	@Before
	public void setUp() {
		List<Item> list = new ArrayList<Item>();
		list.add(new Item("id1", "value1"));
		
		when(myList.getList((List<Item>) argThat(new ListArgumentMatcher(list)))).thenReturn(list);
		
		List<Item> list2 = new ArrayList<Item>();
		list2.add(new Item("id2", "value2"));
		
		when(myList.getList((List<Item>) argThat(new ListArgumentMatcher(list2)))).thenReturn(list2);
	}
	
	@Test
	public void testList() {
		List<Item> list = new ArrayList<Item>();
		list.add(new Item("id1", "value1"));	
		System.out.println("-----------------------"+myList.getList(list));
		
		List<Item> list2 = new ArrayList<Item>();
		list2.add(new Item("id2", "value2"));
		System.out.println("-----------------------"+myList.getList(list2));
	}
	
	public static class MyList {
		
		public List<Item> getList(List<Item> list) {
			return list;
		}
	}
	
	public static class Item {
		String id;
		String value;
		
		public Item(String id, String value) {
			this.id=id;
			this.value=value;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Item other = (Item) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Item [id=" + id + ", value=" + value + "]";
		}
		
	}
	
	public static class ListArgumentMatcher extends ArgumentMatcher {
		List<Item> list = new ArrayList<Item>();
		
		public ListArgumentMatcher(List<Item> list) {
			this.list = list;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean matches(Object argument) {
			if (argument instanceof List) {
				boolean match = ((List) argument).containsAll(list);
				return match;
			}
			
			return false;
		}
		
	}
	
}
