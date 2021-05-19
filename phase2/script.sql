DROP TABLE Doctor CASCADE;  
DROP TABLE Staff CASCADE;
DROP TABLE request_maintenance CASCADE;
DROP TABLE Department CASCADE; 
DROP TABLE Hospital CASCADE;
DROP TABLE Patient CASCADE;
DROP TABLE searches CASCADE; /*weak entity?*/
DROP TABLE Appointment CASCADE;
DROP TABLE search_appnt CASCADE; /*optional*/
DROP TABLE has CASCADE;
DROP TABLE schedules CASCADE;
DROP TABLE past CASCADE;
DROP TABLE active CASCADE;
DROP TABLE available CASCADE;
DROP TABLE waitlisted CASCADE;

CREATE TABLE Doctor(
	doctor_ID INTEGER NOT NULL,
	name CHAR(30),
	specialty CHAR(25),
	dept_ID INTEGER NOT NULL,
	PRIMARY KEY (doctor_ID),
	FOREIGN KEY (dept_ID) REFERENCES Department
);
CREATE TABLE Staff(
	staff_ID INTEGER NOT NULL,
	name CHAR(30),
	hospital_ID INTEGER NOT NULL,
	PRIMARY KEY (staff_ID),
	FOREIGN KEY (hospital_ID) REFERENCES Hospital
);
CREATE TABLE request_maintenance(
	doctor_ID INTEGER NOT NULL,
	staff_ID INTEGER NOT NULL,
	patient_per_hour INTEGER,
	dept_name CHAR(30),
	time_slot INTERVAL(HOUR_TO_MINUTE),
	PRIMARY KEY (doctor_ID, staff_ID),
	FOREIGN KEY (doctor_ID) REFERENCES Doctor,
	FOREIGN KEY (staff_ID) REFERENCES Staff
);
CREATE TABLE Department(
	dept_ID INTEGER NOT NULL,
	name CHAR(30),
	hospital_ID INTEGER NOT NULL,
	PRIMARY KEY (dept_ID),
	FOREIGN KEY (hospital_ID) REFERENCES Hospital
);
CREATE TABLE Hospital(
	hospital_ID INTEGER NOT NULL,
	name CHAR(30),
	PRIMARY KEY (hospital_ID) 	/*TO-DO: search_appnt??*/
);
CREATE TABLE Patient(
	patient_ID INTEGER NOT NULL,
	name CHAR(30),
	gender CHAR(1),
	age INTEGER,
	address CHAR(40),
	num_appnts INTEGER 	/*TO-DO: search_appnt??*/
);

/*TO-DO: CREATE searches TABLE*/

CREATE TABLE Appointment(
	appnt_ID INTEGER NOT NULL,
	date DATE,
	time_slot INTERVAL(HOUR_TO_MINUTE), 	/*TO-DO: is_a attributes and search_appnt*/
	PRIMARY KEY (appnt_ID)
);
CREATE TABLE has(
	doctor_ID INTEGER NOT NULL,
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (doctor_ID, appnt_ID),
	FOREIGN KEY (doctor_ID) REFERENCES Doctor,
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
CREATE TABLE schedules(
	staff_ID INTEGER NOT NULL,
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (staff_ID, appnt_ID),
	FOREIGN KEY (staff_ID) REFERENCES Staff,
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
CREATE TABLE past(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (appnt_ID),
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
CREATE TABLE active(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (appnt_ID),
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
CREATE TABLE available(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (appnt_ID),
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
CREATE TABLE waitlisted(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY (appnt_ID),
	FOREIGN KEY (appnt_ID) REFERENCES Appointment
);
