package ua.profitsoft.demojdbc;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ua.profitsoft.demojdbc.dbutil.MySqlDBUtil;
import ua.profitsoft.demojdbc.model.Group;
import ua.profitsoft.demojdbc.service.JdbcService;

@SpringBootTest
class DemoJdbcApplicationTests {

	@Autowired
	private JdbcService jdbcService;


	@Test
	public void testPool()	{
		List<Group> list = buildBigList();
		Long t = System.currentTimeMillis();
		list.forEach(g -> jdbcService.createGroup(g));
		System.out.println("Time without pool: " + (System.currentTimeMillis() - t));

		List<Group> listPool = buildBigList();
		t = System.currentTimeMillis();
		listPool.forEach(g -> jdbcService.createGroupWithPool(g));
		System.out.println("Time with pool: " + (System.currentTimeMillis() - t));
	}

	@Test
	public void fillData() {
		jdbcService.fillTestData();
	}


	@Test
	public void testBatch() {
		List<Group> list = buildBigList();
		Long t = System.currentTimeMillis();
		jdbcService.saveListByOne(list);
		System.out.println("Time by one: " + (System.currentTimeMillis() - t));
		jdbcService.deleteAll();
		t = System.currentTimeMillis();
		jdbcService.saveListByBatch(list);
		System.out.println("Time by batch: " + (System.currentTimeMillis() - t));
		jdbcService.deleteAll();
	}

	private List<Group> buildBigList() {
		List<Group> list = new ArrayList<>();
		//big list
		for (int course = 1; course < 7; course++) {
			for (int gr = 1; gr < 1000; gr++) {
				list.add(Group.builder().
						name("Group" + gr + "cr" + course).
						course(course).
						build());
			}
		}
		return list;
	}

	@Test
	public void testJdbcGroup()	{
		//save groups
		for (int course = 1; course < 7; course++) {
			for (int gr = 1; gr < 5; gr++) {
				Group group = Group.builder().
						name("Group" + course + gr).
						course(course).
						build();
				jdbcService.saveGroup(group);
			}
		}

		List<Group> listGroup = jdbcService.getAllGroups();
		System.out.println("Total groups count: " + listGroup.size());
		listGroup.forEach(g -> System.out.println(g.toString()));

		Group gC2G3 = jdbcService.getGroupByName("Group23");
		gC2G3.setName("ChangedNameGroup23");
		jdbcService.saveGroup(gC2G3);

		Group gChanged = jdbcService.getGroupByName("ChangedNameGroup23");
		System.out.println("--- Changed group ----");
		System.out.println(gChanged.toString());

		jdbcService.deleteGroup(gChanged.getId());

		listGroup = jdbcService.getAllGroups();
		System.out.println("Total groups count after delete: " + listGroup.size());
		listGroup.forEach(g -> jdbcService.deleteGroup(g.getId()));
	}

	@Test
	public void testJdbcTemplateGroup()	{
		for (int course = 1; course < 7; course++) {
			for (int gr = 1; gr < 5; gr++) {
				Group group = Group.builder().
						name("Group" + course + gr).
						course(course).
						build();
				jdbcService.createGroupWithJdbcTemplate(group);
			}
		}

		List<Group> listGroup = jdbcService.getAllGroupsJdbcTemplate();
		System.out.println("Total groups count: " + listGroup.size());

		Group gC3G3 = jdbcService.getGroupByNameJdbcTemplate("Group33");
		System.out.println("After read: " + gC3G3);
		gC3G3.setName("ChangedGoupName33");
		jdbcService.updateGroupJdbcTemplate(gC3G3);
		Group changedC3G3 = jdbcService.getGroupByNameJdbcTemplate("ChangedGoupName33");
		System.out.println("After change: " + changedC3G3);
		listGroup.forEach(g -> jdbcService.deleteGroup(g.getId()));

	}

}
