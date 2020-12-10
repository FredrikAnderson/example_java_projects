package com.fredrik.bookit.model.mapper;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fredrik.bookit.hello.IntegrationTest;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@IntegrationTest
@Slf4j
@Ignore
public class ItemMapperTest {

//	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
//	Mapper itemMapper;
	
//	@Autowired
//	ItemMapper itemMapper;
//	
//	@Test
//	public void fullItemToItemProperties_correctly() {
//		// Given
//		Item item = new Item();
//		item.setPublicId("EAN12345678");
//		item.setInventory("Hylla 2A");
//		
//		ItemProperties properties = new ItemProperties();
//		properties.setName("Hammare");
//		properties.setDescription("Item test");
//		properties.setHeight(12.34);
//		properties.setWidth(43.34);
//		properties.setLength(123.0);
//		properties.setWeight(1.345);
//		properties.setPrice(99.95);
//		item.setProperties(properties);
//
//		// When
//		ItemDTO itemDTO = itemMapper.mapEntityToDTO(item);
//		
//		
//		// Then
//		assertEquals("Hammare", itemDTO.getName());
//		assertEquals("Item test", itemDTO.getDescription());
//		assertEquals("EAN12345678", itemDTO.getPublicId());		
//		assertEquals("Hylla 2A", itemDTO.getInventory());		
//		
//		assertEquals(new Float(12.34), itemDTO.getHeight());
//		assertEquals(new Float(123.0), itemDTO.getLength());
//		assertEquals(new Float(99.95), itemDTO.getPrice());		
//		assertEquals(new Float(43.34), itemDTO.getWidth());		
//		assertEquals(new Float(1.345), itemDTO.getWeight());		
//		
//	}

}