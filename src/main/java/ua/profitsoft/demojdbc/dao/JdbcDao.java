package ua.profitsoft.demojdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ua.profitsoft.demojdbc.dbutil.MySqlDBUtil;
import ua.profitsoft.demojdbc.model.Group;

@Repository
@RequiredArgsConstructor
public class JdbcDao {

	@Autowired
	private MySqlDBUtil mySqlDBUtil;


	private final NamedParameterJdbcTemplate jdbcTemplate;

	//poollable DataSource (Hikari)
	@Autowired
	private DataSource dataSource;

	@PostConstruct
	public void init() {
		//jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void fillTestData() {
		Random r = new Random();
		List<Group> grpList = new ArrayList<>();
		for (int course = 1; course < 7; course++) {
			for (int gr = 1; gr < 5; gr++) {
				Group group = Group.builder().
						name("Group" + course + gr).
						course(course).
						build();
				saveGroup(group);
				grpList.add(group);
			}
		}
		try(Connection conn = dataSource.getConnection();
		    PreparedStatement stmtStud = conn.prepareStatement("insert into student (first_name, last_name, id_grp) values (?, ?, ?)");
		    PreparedStatement stmtSubject = conn.prepareStatement("insert into subject (name, hours) values (?, ?)");
		    PreparedStatement stmtSubjectStud = conn.prepareStatement("insert into student_subject (id_student, id_subject) values (?, ?)");
		    Statement stmt = conn.createStatement()){
			List<Long> studIds = new ArrayList<>();
			for (Group g: grpList) {
				for (int i = 0; i < 100; i++) {
					stmtStud.setString(1, "StudName" + i + "_" + g.getId());
					stmtStud.setString(2, "StudSurname" + i);
					stmtStud.setLong(3, g.getId());
					stmtStud.executeUpdate();
					ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
					rs.next();
					studIds.add(rs.getLong(1));
				}
			}
			List<Long> subjIds = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				stmtSubject.setString(1, "SubjectName" + i);
				stmtSubject.setInt(2, r.nextInt(20));
				stmtSubject.executeUpdate();
				ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
				rs.next();
				subjIds.add(rs.getLong(1));

			}
			for (Long studId: studIds) {
				for (Long subjId: subjIds) {
					stmtSubjectStud.setLong(1, studId);
					stmtSubjectStud.setLong(2, subjId);
					stmtSubjectStud.executeUpdate();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Fill data Error", e);
		}

	}

	public void saveGroup(Group grp) {
		if (grp.getId() == null) {
			Long grpId = createGroup(grp);
			grp.setId(grpId);
		} else {
			updateGroup(grp);
		}
	}

	public void deleteGroup(Long grpId) {
		try(Connection conn = mySqlDBUtil.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("delete from grp where id = ?")){
			stmt.setLong(1, grpId);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("deleteGroup Error", e);
		}
	}

	public void deleteAll() {
		try(Connection conn = mySqlDBUtil.getConnection();
		    Statement stmt = conn.createStatement()){
			stmt.execute("delete from grp");
		} catch (Exception e) {
			throw new RuntimeException("deleteAll Error", e);
		}
	}

	public Group getGroupByName(String grpName) {
		try(Connection conn = mySqlDBUtil.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("select id, name, course from grp where name = ?")){
			stmt.setString(1, grpName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapGroup(rs);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("getGroupById Error", e);
		}
	}

	public Group getGroupByNameJdbcTemplate(String grpName) {
		return jdbcTemplate.queryForObject("select id, name, course from grp where name = :name",
				new MapSqlParameterSource().addValue("name", grpName),
				(rs, rowNum) -> mapGroup(rs));
	}

	public List<Group> getAllGroupsJdbcTemplate() {
		return jdbcTemplate.query("select id, name, course from grp", (rs, rowNum) -> mapGroup(rs));
	}

	public List<Group> getAllGroups() {
		List<Group> result = new ArrayList<>();
		try(Connection conn = mySqlDBUtil.getConnection();
		    Statement stmt = conn.createStatement()){
			ResultSet rs = stmt.executeQuery("select id, name, course from grp");
			while (rs.next()) {
				result.add(mapGroup(rs));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException("getGroupById Error", e);
		}
	}

	public void saveListByOne(List<Group> list) {
		try(Connection conn = mySqlDBUtil.getConnection()){
			list.forEach(g -> {
				try (Statement stmt = conn.createStatement()) {
					stmt.execute("insert into grp (name, course) values ('" + g.getName() + "', " + g.getCourse() + ")");
				} catch (Exception e) {
				throw new RuntimeException("saveListByOne single Error", e);
			}

		});
		} catch (Exception e) {
			throw new RuntimeException("saveListByOne Error", e);
		}
	}

	public void saveListByBatch(List<Group> list) {
		try(Connection conn = mySqlDBUtil.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("insert into grp (name, course) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
			list.forEach(g -> {
				try {
					stmt.setString(1, g.getName());
					stmt.setInt(2, g.getCourse());
					stmt.addBatch();
				} catch (SQLException e) {
					throw new RuntimeException("saveListByBatch addButch Error", e);
				}

			});
			stmt.executeBatch();
		} catch (Exception e) {
			throw new RuntimeException("saveListByBatch Error", e);
		}
	}

	private Group mapGroup(ResultSet rs) throws SQLException {
		return Group.builder()
		.id(rs.getLong("id"))
		.name(rs.getString("name"))
		.course(rs.getInt("course")).build();
	}

	private void updateGroup(Group grp) {
		try(Connection conn = mySqlDBUtil.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("update grp set name = ?, course = ? where id = ?")){
			stmt.setString(1, grp.getName());
			stmt.setInt(2, grp.getCourse());
			stmt.setLong(3, grp.getId());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("update Group Error", e);
		}
	}

	public void updateGroupJdbcTemplate(Group grp) {
		jdbcTemplate.update("update grp set name = :name, course = :course where id = :id",
				new MapSqlParameterSource()
						.addValue("name", grp.getName())
						.addValue("course", grp.getCourse())
						.addValue("id", grp.getId())
		);
	}


	public Long createGroup(Group grp) {
		try(Connection conn = mySqlDBUtil.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("insert into grp (name, course) values (?, ?)", Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, grp.getName());
			stmt.setInt(2, grp.getCourse());
			stmt.executeUpdate();
			ResultSet gk = stmt.getGeneratedKeys();
			if (gk.next()) {
				return gk.getLong(1);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException("Create Group Error", e);
		}
	}

	public Long createGroupWithPool(Group grp) {
		try(Connection conn = dataSource.getConnection();
		    PreparedStatement stmt = conn.prepareStatement("insert into grp (name, course) values (?, ?)", Statement.RETURN_GENERATED_KEYS)){
			stmt.setString(1, grp.getName());
			stmt.setInt(2, grp.getCourse());
			stmt.executeUpdate();
			ResultSet gk = stmt.getGeneratedKeys();
			if (gk.next()) {
				return gk.getLong(1);
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException("Create Group Error", e);
		}
	}

	public void createGroupWithJdbcTemplate(Group grp) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update("insert into grp (name, course) values (:name , :course)",
				new MapSqlParameterSource()
						.addValue("name", grp.getName())
						.addValue("course", grp.getCourse()),
				keyHolder);
		grp.setId(keyHolder.getKey().longValue());
	}

}
