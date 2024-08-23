package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		List<Department> list = new ArrayList<>();
		DepartmentDao depDao = DaoFactory.createDepartmentDao();

		System.out.println("==== TEST 1: dao findById ====");
		Department dep = depDao.findById(2);
		System.out.println(dep);

		System.out.println("\n==== TEST 2: dep findAll ====");
		list = depDao.findAll();
		list.forEach(System.out::println);

		System.out.println("\n==== TEST 3: dep insert ====");
		Department newDepartment = new Department(null, "Videogames");
		depDao.insert(newDepartment);
		System.out.println("Inserted! New id = " + newDepartment.getId());

		System.out.println("\n==== TEST 4: dep update ====");
		dep = depDao.findById(1);
		dep.setName("Jogos");
		depDao.update(dep);
		System.out.println("Update complete!");

		System.out.println("\n==== TEST 5: dep delete ====");
		System.out.print("Enter id for delete test: ");
		int id = sc.nextInt();
		depDao.deleteById(id);
		System.out.println("Delete completed!");

		sc.close();
		DB.closeConnection();
	}

}
