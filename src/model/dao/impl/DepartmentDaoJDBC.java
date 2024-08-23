package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {

		PreparedStatement st = null;
		try {

			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rows = st.executeUpdate();

			if (rows > 0) {
				ResultSet rs = st.getGeneratedKeys();

				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}

				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);

		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {

			st = conn.prepareStatement("UPDATE department SET Name = ? where id = ?", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			int rows = st.executeUpdate();

			if (rows > 0) {
				ResultSet rs = st.getGeneratedKeys();

				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}

				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);

		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from department where id = ?");

			st.setInt(1, id);

			int row = st.executeUpdate();

			if (row == 0) {
				throw new DbException("Id does not exists");
			}

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("select department.* from department where Id = ?");

			st.setInt(1, id);

			rs = st.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		return null;
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {

			st = conn.prepareStatement("select department.* from department");

			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();

			while (rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
		return dep;
	}
}
