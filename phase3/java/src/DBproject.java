/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject {
	// reference to physical database connection
	public Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try {
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println("Connection URL: " + url + "\n");

			// obtain a physical connection
			this._connection = DriverManager.getConnection(url, user, passwd);
			System.out.println("Done");
		} catch (Exception e) {
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
			System.out.println("Make sure you started postgres on this machine");
			System.exit(-1);
		}
	}

	/**
	 * Method to execute an update SQL statement. Update SQL instructions includes
	 * CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL String
	 * @throws java.sql.SQLException when update failed
	 */
	public void executeUpdate(String sql) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the update instruction
		stmt.executeUpdate(sql);

		// close the instruction
		stmt.close();
	}// end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT). This method
	 * issues the query to the DBMS and outputs the results to standard out.
	 * 
	 * @param query the input query String
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);

		/*
		 * obtains the metadata object for the returned result set. The metadata
		 * contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCol = rsmd.getColumnCount();
		int rowCount = 0;

		// iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()) {
			if (outputHeader) {
				for (int i = 1; i <= numCol; i++) {
					System.out.print(rsmd.getColumnName(i) + "\t");
				}
				System.out.println();
				outputHeader = false;
			}
			for (int i = 1; i <= numCol; ++i)
				System.out.print(rs.getString(i) + "\t");
			System.out.println();
			++rowCount;
		} // end while
		stmt.close();
		return rowCount;
	}

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT). This method
	 * issues the query to the DBMS and returns the results as a list of records.
	 * Each record in turn is a list of attribute values
	 * 
	 * @param query the input query String
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);

		/*
		 * obtains the metadata object for the returned result set. The metadata
		 * contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCol = rsmd.getColumnCount();
		int rowCount = 0;

		// iterates through the result set and saves the data returned by the query.
		boolean outputHeader = false;
		List<List<String>> result = new ArrayList<List<String>>();
		while (rs.next()) {
			List<String> record = new ArrayList<String>();
			for (int i = 1; i <= numCol; ++i)
				record.add(rs.getString(i));
			result.add(record);
		} // end while
		stmt.close();
		return result;
	}// end executeQueryAndReturnResult

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT). This method
	 * issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query String
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);

		int rowCount = 0;

		// iterates through the result set and count nuber of results.
		if (rs.next()) {
			rowCount++;
		} // end while
		stmt.close();
		return rowCount;
	}

	/**
	 * Method to fetch the last value from sequence. This method issues the query to
	 * the DBMS and returns the current value of sequence used for autogenerated
	 * keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */

	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement();

		ResultSet rs = stmt.executeQuery(String.format("SELECT currval('%s');", sequence));
		if (rs.next())
			return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup() {
		try {
			if (this._connection != null) {
				this._connection.close();
			} // end if
		} catch (SQLException e) {
			// ignored.
		} // end try
	}// end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login
	 *             file>
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName()
					+ " <dbname> <port> <user>");
			return;
		} // end if

		DBproject esql = null;

		try {
			System.out.println("(1)");

			try {
				Class.forName("org.postgresql.Driver");
			} catch (Exception e) {

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}

			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];

			esql = new DBproject(dbname, dbport, user, "");

			boolean keepon = true;
			while (keepon) {
				try {

					System.out.println("MAIN MENU");
					System.out.println("---------");
					System.out.println("1. Add Doctor");
					System.out.println("2. Add Patient");
					System.out.println("3. Add Appointment");
					System.out.println("4. Make an Appointment");
					System.out.println("5. List appointments of a given doctor");
					System.out.println("6. List all available appointments of a given department");
					System.out.println(
							"7. List total number of different types of appointments per doctor in descending order");
					System.out.println("8. Find total number of patients per doctor with a given status");
					System.out.println("9. < EXIT");

					switch (readInt("choice")) {
						case 1:
							AddDoctor(esql);
							break;
						case 2:
							AddPatient(esql);
							break;
						case 3:
							AddAppointment(esql);
							break;
						case 4:
							MakeAppointment(esql);
							break;
						case 5:
							ListAppointmentsOfDoctor(esql);
							break;
						case 6:
							ListAvailableAppointmentsOfDepartment(esql);
							break;
						case 7:
							ListStatusNumberOfAppointmentsPerDoctor(esql);
							break;
						case 8:
							FindPatientsCountWithStatus(esql);
							break;
						case 9:
							keepon = false;
							break;
					}
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup();
					System.out.println("Done\n\nBye !");
				} // end if
			} catch (Exception e) {
				// ignored.
			}
		}
	}

	public static int readInt(String name) {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print(String.format("Enter %s: ", name));
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			} // end try
		} while (true);
		return input;
	}

	public static String readString(String name, int limit, String[] allowed) {
		try {
			do {
				System.out.print(String.format("Enter %s (max %d chars): ", name, limit));
				String input = in.readLine();
				if (input.length() <= limit && contains(allowed, input))
					return input;
				System.out.println("Invalid choice.");
			} while (true);
		} catch (IOException e) {
			System.out.println("Exception occured with buffered reader");
			System.exit(2);
		}
		return null;
	}

	public static String readString(String name, int limit){
		return readString(name, limit, null);
	}

	public static void AddDoctor(DBproject esql) throws SQLException {// 1
		String name = readString("doctor name", 128);
		String specialty = readString("doctor specialty", 24);
		int departmentID = readInt("doctor department id");

		String query = "INSERT INTO Doctor(name, specialty, did) VALUES(?, ?, ?);";
		PreparedStatement stmt = esql._connection.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setString(2, specialty);
		stmt.setInt(3, departmentID);
		stmt.executeUpdate();

		System.out.println("Successfully added doctor.\n");
	}

	public static void AddPatient(DBproject esql) throws SQLException {// 2
		String name = readString("patient name", 128);
		String gender = readString("patient gender ('F' or 'M')", 1, new String[] { "F", "M" });
		int age = readInt("age");
		String address = readString("address", 256);

		String query = "INSERT INTO Patient(name, gtype, age, address, number_of_appts) VALUES(?, ?, ?, ?, 0);";
		PreparedStatement stmt = esql._connection.prepareStatement(query);
		stmt.setString(1, name);
		stmt.setString(2, gender);
		stmt.setInt(3, age);
		stmt.setString(4, address);
		stmt.executeUpdate();

		System.out.println("Successfully added patient.\n");
	}

	public static void AddAppointment(DBproject esql) throws SQLException {// 3
		String date = readString("appointment date", 10);
		String time = readString("time slot", 11);
		String status = readString("appointment status", 2, new String[] { "PA", "AC", "AV", "WL" });
		int did = readInt("doctor id");

		String query = "INSERT INTO Appointment(adate, time_slot, status) VALUES(?, ?, ?);";
		PreparedStatement stmt = esql._connection.prepareStatement(query);
		stmt.setDate(1, java.sql.Date.valueOf(date));
		stmt.setString(2, time);
		stmt.setString(3, status);
		stmt.executeUpdate();

		int newID = esql.getCurrSeqVal("appointment_appnt_id_seq");
		esql.executeUpdate(String.format("INSERT INTO has_appointment(appt_id, doctor_id) VALUES (%d, %d)", newID, did));

		System.out.println(String.format("Successfully added appointment %d.\n", newID));
	}

	public static void MakeAppointment(DBproject esql) throws SQLException {// 4
		// Given a patient, a doctor and an appointment of the doctor that s/he wants to
		// take, add an appointment to the DB
		int patientID = readInt("patient id");
		int doctorID = readInt("doctor id");
		int appntID = readInt("appointment id");

		List<List<String>> results = esql.executeQueryAndReturnResult(String.format("SELECT adate, time_slot, status FROM Appointment WHERE appnt_id=%d;", appntID));
		List<String> appointment = results.get(0);
		String status = appointment.get(2);
		System.out.println(String.format("The status of this appointment is %s.", status));

		if (status.equals("AV")) {
			// update status to active
			esql.executeUpdate(String.format("UPDATE Appointment SET status='AC' WHERE appnt_id=%d;", appntID));

			// add the appointment
			esql.executeUpdate(String.format("INSERT INTO has_appointment(appt_id, doctor_id) VALUES (%d, %d);", appntID, doctorID));
			System.out.println(String.format("Successfully reserved appointment."));
		} else if (status.equals("AC") || status.equals("WL")) {
			System.out.println("Appointment is already reserved, adding to the waitlist.");

			// waitlist this appointment
			esql.executeUpdate(String.format("INSERT INTO Appointment(adate, time_slot, status) VALUES('%s', '%s', '%s');", appointment.get(0), appointment.get(1), "WL"));

			int newID = esql.getCurrSeqVal("appointment_appnt_id_seq");
			esql.executeUpdate(String.format("INSERT INTO has_appointment(appt_id, doctor_id) VALUES (%d, %d);", newID, doctorID));
			System.out.println(String.format("Successfully waitlisted new appointment. Appointment ID: %d", newID));
		} else {
			System.out.println(String.format("Invalid appointment status, not doing anything."));
			return;
		}

		// increase the amount of appointments the patient has
		esql.executeUpdate(String.format("UPDATE Patient SET number_of_appts=number_of_appts+1 WHERE patient_ID=%d;", patientID));
		System.out.println();
	}

	public static void ListAppointmentsOfDoctor(DBproject esql) throws SQLException {// 5
		// For a doctor ID and a date range, find the list of active and available
		// appointments of the doctor

		int did = readInt("doctor ID");
		String bdate = readString("beginning date of date range", 24);
		String edate = readString("end date of date range", 24);
		
		System.out.println(String.format("Doctor %d has the following active and available appointments in this date range:", did));
		esql.executeQueryAndPrintResult(String.format("SELECT appnt_id, adate, time_slot, status FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id=has_appointment.appt_id WHERE has_appointment.doctor_id=%d AND (Appointment.status='AV' OR Appointment.status='AC') AND (Appointment.adate BETWEEN '%s' and '%s');", did, bdate, edate));
		System.out.println();
	}

	public static void ListAvailableAppointmentsOfDepartment(DBproject esql) throws SQLException {// 6
		// For a department name and a specific date, find the list of available
		// appointments of the department
		String dname = readString("department name", 32);
		String date = readString("date", 24);
		
		// get the hospital id of the department
		List<List<String>> departments = esql.executeQueryAndReturnResult(String.format("SELECT dept_id, hid FROM Department WHERE Department.name='%s';", dname));
		for (List<String> row : departments) {
			int did = Integer.parseInt(row.get(0));
			int hid = Integer.parseInt(row.get(1));

			System.out.println(String.format("The %s department in hospital %d has the following appointments available on that date:", dname, hid));
			esql.executeQueryAndPrintResult(String.format("SELECT appnt_id, adate, time_slot FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id=has_appointment.appt_id INNER JOIN Doctor ON has_appointment.doctor_id=Doctor.doctor_id WHERE Appointment.adate='%s' AND Doctor.did=%d AND Appointment.status='AV';", date, did));
			System.out.println();
		}

		System.out.println();
	}

	public static void ListStatusNumberOfAppointmentsPerDoctor(DBproject esql) throws SQLException {// 7
		// Count number of different types of appointments per doctors and list them in
		// descending order
		
		String[] appointmentTypes = new String[] { "WL", "PA", "AC", "AV" };
		// a mapping of doctor_id: <wl, pa, ac, av>
		HashMap<Integer, List<Integer>> doctorAppointments = new HashMap<Integer, List<Integer>>();

		List<List<String>> WLList = esql.executeQueryAndReturnResult(String.format("SELECT doctor_id, COUNT(*) FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id = has_appointment.appt_id WHERE Appointment.status='WL' GROUP BY doctor_id;"));
		PopulateDoctorAppointments(doctorAppointments, WLList, 0);
		
		List<List<String>> PAList = esql.executeQueryAndReturnResult(String.format("SELECT doctor_id, COUNT(*) FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id = has_appointment.appt_id WHERE Appointment.status='WL' GROUP BY doctor_id;"));
		PopulateDoctorAppointments(doctorAppointments, PAList, 1);
		
		List<List<String>> ACList = esql.executeQueryAndReturnResult(String.format("SELECT doctor_id, COUNT(*) FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id = has_appointment.appt_id WHERE Appointment.status='AC' GROUP BY doctor_id;"));
		PopulateDoctorAppointments(doctorAppointments, ACList, 2);
		
		List<List<String>> AVList = esql.executeQueryAndReturnResult(String.format("SELECT doctor_id, COUNT(*) FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id = has_appointment.appt_id WHERE Appointment.status='AV' GROUP BY doctor_id;"));
		PopulateDoctorAppointments(doctorAppointments, AVList, 3);

		for (Map.Entry<Integer, List<Integer>> e : doctorAppointments.entrySet()) {
			List<Integer> numAppts = e.getValue();
			// sort the number of appts each doctor has
			// https://stackoverflow.com/a/35718576
			int[] sortedIndices = IntStream.range(0, numAppts.size())
				.boxed()
				.sorted((i, j) -> numAppts.get(j) - numAppts.get(i))
				.mapToInt(x -> x).toArray();

			List<String> sortedNumAppts = new ArrayList<String>();
			for (int i : sortedIndices) {
				sortedNumAppts.add(String.format("%d %s", numAppts.get(i), appointmentTypes[i]));
			}
			System.out.println(String.format("Doctor %d: %s", e.getKey(), String.join(", ", sortedNumAppts)));
		}

		System.out.println();
	}

	private static void PopulateDoctorAppointments(HashMap<Integer, List<Integer>> doctorAppointments, List<List<String>> queryResult, int index) {
		for (List<String> row : queryResult) {
			int doctorID = Integer.parseInt(row.get(0));
			int amount = Integer.parseInt(row.get(1));

			List<Integer> appointments = doctorAppointments.get(doctorID);
			if (appointments == null) { // first time adding this doctor in
				List<Integer> newAppts = Arrays.asList(0, 0, 0, 0);
				doctorAppointments.put(doctorID, newAppts);
				appointments = newAppts;
			}

			appointments.set(index, amount);
		}
	}

	public static void FindPatientsCountWithStatus(DBproject esql) throws SQLException {// 8
		// Find how many patients per doctor there are with a given status (i.e. PA, AC,
		// AV, WL) and list that number per doctor.

		String status = readString("appointment status", 2, new String[] { "PA", "AC", "AV", "WL" });
		String query = String.format("SELECT doctor_id, COUNT(*) FROM Appointment INNER JOIN has_appointment ON Appointment.appnt_id=has_appointment.appt_id WHERE Appointment.status='%s' GROUP BY doctor_id;", status);

		List<List<String>> results = esql.executeQueryAndReturnResult(query);
		for (List<String> row : results) {
			System.out.println(String.format("Doctor %s has %s %s appointments.", row.get(0), row.get(1), status));
		}
		System.out.println();
	}

	// utility functions
	public static boolean contains(String[] allowed, String choice) {
		if (allowed == null)
			return true;
		for (String a : allowed) {
			if (choice.equals(a))
				return true;
		}
		return false;
	}
}