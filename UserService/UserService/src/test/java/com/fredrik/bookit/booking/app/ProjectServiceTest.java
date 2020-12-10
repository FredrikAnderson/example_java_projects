package com.fredrik.bookit.booking.app;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fredrik.bookit.hello.IntegrationTest;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@IntegrationTest
@Slf4j
@Ignore
public class ProjectServiceTest {

//	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

//	@Inject 
//	private ProjectService projectService;
//
//	@Inject 
//	private ItemService itemService;
//
//	@Test 
//	public void twoTimeOverlappingProjects_bookSameItemToBoth_lastProjectShouldNotBookItem() {
//		// Arrange/Given
//		List<ProjectDTO> projects = projectService.getProjects();
//		int sizeBefore = projects.size();
//		
//		ProjectDTO p1 = new ProjectDTO();
//		p1.setName("P1");
//		p1.setStartDate(LocalDate.of(2020, 04, 01));
//		p1.setEndDate(LocalDate.of(2020, 05, 01));
//		p1.setBookedItems(Lists.emptyList());
//		p1 = projectService.saveProject(p1);
//
//		ProjectDTO p2 = new ProjectDTO();
//		p2.setName("P2");
//		p2.setStartDate(LocalDate.of(2020, 04, 15));
//		p2.setEndDate(LocalDate.of(2020, 05, 15));
//		p2.setBookedItems(Lists.emptyList());
//		p2 = projectService.saveProject(p2);
//
//		projects = projectService.getProjects();
//		int sizeAfter = projects.size();
//		
//		// Then 2 projects should have been saved
//		assertEquals(2, sizeAfter - sizeBefore); 		
//		
//		ItemDTO itemDTO = itemService.findAll().get(0);		
//		
//		// Act/When
//		p1 = projectService.bookItemToProject(p1.getId(), itemDTO.getId());
//		try {
//			p2 = projectService.bookItemToProject(p2.getId(), itemDTO.getId());
//		} catch (Throwable thr) {
//			log.info ("Correctly got an exception trying to book same time: ", thr);
//		}
//
//		itemDTO = itemService.findOne(itemDTO.getId());		
//		
//		// Assert/Then
//		assertEquals(1, itemDTO.getProjects().size()); 		
//				
//	}
//
//	@Test 
//	public void givenAFilter_findItems_correct() {
//		// Given
//		
////		// save a couple of items
////		ItemProperties props = ItemProperties.builder()
////			.name("Stor hammare")
////			.description("Stor hammare")
//////			.id()
////			.height(1.0)
////			.width(2.0)
////			.length(3.0)
////			.build();
////
////		Item entity = new Item(0L, "EAN 1234", props, "Hylla 2A");
////		ItemDTO itemDTO = itemMapper.mapEntityToDTO(entity);
//		
//		// When
////		List<ItemDTO> list = projectService.findBy("ham");
////
////		assertEquals(1, list.size()); 		
////
////		list = projectService.findBy("gam");
////
////		assertEquals(0, list.size()); 		
//		
//	}
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