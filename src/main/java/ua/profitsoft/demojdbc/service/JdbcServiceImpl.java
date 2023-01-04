package ua.profitsoft.demojdbc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.profitsoft.demojdbc.dao.JdbcDao;
import ua.profitsoft.demojdbc.model.Group;

@Service
@Transactional
public class JdbcServiceImpl implements JdbcService {

	@Autowired
	private JdbcDao dao;

	@Override
	public void saveGroup(Group grp) {
		dao.saveGroup(grp);

	}

	@Override
	public void deleteGroup(Long grpId) {
		dao.deleteGroup(grpId);
	}

	@Override
	public void deleteAll() {
		dao.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Group> getAllGroups() {
		return dao.getAllGroups();
	}

	@Override
	@Transactional(readOnly = true)
	public Group getGroupByName(String grpName) {
		return dao.getGroupByName(grpName);
	}

	@Override
	public void saveListByBatch(List<Group> list) {
		dao.saveListByBatch(list);
	}

	@Override
	public void saveListByOne(List<Group> list) {
		dao.saveListByOne(list);
	}

	@Override
	public Long createGroup(Group grp) {
		return dao.createGroup(grp);
	}

	@Override
	public Long createGroupWithPool(Group grp) {
		return dao.createGroupWithPool(grp);
	}

	@Override
	public void fillTestData() {
		dao.fillTestData();
	}

	@Override
	@Transactional(readOnly = true)
	public Group getGroupByNameJdbcTemplate(String grpName) {
		return dao.getGroupByNameJdbcTemplate(grpName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Group> getAllGroupsJdbcTemplate() {
		return dao.getAllGroupsJdbcTemplate();
	}

	@Override
	public void updateGroupJdbcTemplate(Group grp) {
		dao.updateGroupJdbcTemplate(grp);
	}

	@Override
	public void createGroupWithJdbcTemplate(Group grp) {
		dao.createGroupWithJdbcTemplate(grp);
	}

}
