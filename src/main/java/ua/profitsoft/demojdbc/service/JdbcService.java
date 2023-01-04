package ua.profitsoft.demojdbc.service;

import java.util.List;

import ua.profitsoft.demojdbc.model.Group;

public interface JdbcService {

	void saveGroup(Group grp);

	void deleteGroup(Long grpId);

	void deleteAll();

	List<Group> getAllGroups();

	Group getGroupByName(String grpName);

	void saveListByBatch(List<Group> list);

	void saveListByOne(List<Group> list);

	Long createGroup(Group grp);

	Long createGroupWithPool(Group grp);

	void fillTestData();

	Group getGroupByNameJdbcTemplate(String grpName);

	List<Group> getAllGroupsJdbcTemplate();

	void updateGroupJdbcTemplate(Group grp);

	void createGroupWithJdbcTemplate(Group grp);

}
