/* Entities */
DROP TABLE IF EXISTS Appointment CASCADE;
DROP TABLE IF EXISTS Past_Appointment CASCADE;
DROP TABLE IF EXISTS Active_Appointment CASCADE;
DROP TABLE IF EXISTS Available_Appointment CASCADE;
DROP TABLE IF EXISTS Waitlisted_Appointment CASCADE;

DROP TABLE IF EXISTS Department CASCADE; 
DROP TABLE IF EXISTS Doctor CASCADE;  
DROP TABLE IF EXISTS Hospital CASCADE;
DROP TABLE IF EXISTS Patient CASCADE;
DROP TABLE IF EXISTS Staff CASCADE;

/* Relationships */
DROP TABLE IF EXISTS request_maintenance CASCADE;
DROP TABLE IF EXISTS searches CASCADE;

/* Entities */
CREATE TABLE Hospital(
	hospital_ID INTEGER NOT NULL,
	name VARCHAR,

	PRIMARY KEY(hospital_ID)
);

CREATE TABLE Department(
	dept_ID INTEGER NOT NULL,
	name VARCHAR,

	/* foreign keys */
	hospital_ID INTEGER NOT NULL, /* includes */
	
	PRIMARY KEY(dept_ID),
	FOREIGN KEY(hospital_ID) REFERENCES Hospital
);

CREATE TABLE Doctor(
	doctor_ID INTEGER NOT NULL,
	name VARCHAR,
	specialty VARCHAR,

	/* foreign keys */
	dept_ID INTEGER NOT NULL, /* works_dept */

	PRIMARY KEY(doctor_ID),
	FOREIGN KEY(dept_ID) REFERENCES Department
);

CREATE TABLE Staff(
	staff_ID INTEGER NOT NULL,
	name VARCHAR,

	/* foreign keys */
	hospital_ID INTEGER NOT NULL, /* works_in */

	PRIMARY KEY(staff_ID),
	FOREIGN KEY(hospital_ID) REFERENCES Hospital
);

CREATE TABLE Patient(
	patient_ID INTEGER NOT NULL,
	name VARCHAR,
	gender VARCHAR,
	age INTEGER,
	address VARCHAR,
	num_appnts INTEGER,
	
	PRIMARY KEY(patient_ID)
);

CREATE TABLE Appointment(
	appnt_ID INTEGER NOT NULL,
	date DATE,
	time_slot TSRANGE,
	type VARCHAR NOT NULL, /* used to choose the table from past, active, available, waitlisted */

	/* foreign keys */
	doctor_ID INTEGER NOT NULL, /* has */
	staff_ID INTEGER NOT NULL, /* schedules */

	PRIMARY KEY(appnt_ID),
	FOREIGN KEY(doctor_ID) REFERENCES Doctor,
	FOREIGN KEY(staff_ID) REFERENCES Staff
);

CREATE TABLE Past_Appointment(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY(appnt_ID),
	FOREIGN KEY(appnt_ID) REFERENCES Appointment
);
CREATE TABLE Active_Appointment(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY(appnt_ID),
	FOREIGN KEY(appnt_ID) REFERENCES Appointment
);
CREATE TABLE Available_Appointment(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY(appnt_ID),
	FOREIGN KEY(appnt_ID) REFERENCES Appointment
);
CREATE TABLE Waitlisted_Appointment(
	appnt_ID INTEGER NOT NULL,
	PRIMARY KEY(appnt_ID),
	FOREIGN KEY(appnt_ID) REFERENCES Appointment
);

/* relationships */
CREATE TABLE request_maintenance(
	patient_per_hour INTEGER,
	dept_name VARCHAR,
	time_slot TSRANGE,

	/* foreign keys */
	doctor_ID INTEGER NOT NULL,
	staff_ID INTEGER NOT NULL,

	PRIMARY KEY(doctor_ID, staff_ID),
	FOREIGN KEY(doctor_ID) REFERENCES Doctor,
	FOREIGN KEY(staff_ID) REFERENCES Staff
);

CREATE TABLE searches(
	/* foreign keys */
	hospital_ID INTEGER NOT NULL,
	patient_ID INTEGER NOT NULL,
	appnt_ID INTEGER NOT NULL,

	PRIMARY KEY(hospital_ID, patient_ID, appnt_ID),
	FOREIGN KEY(hospital_ID) REFERENCES Hospital,
	FOREIGN KEY(patient_ID) REFERENCES Patient,
	FOREIGN KEY(appnt_ID) REFERENCES Appointment
)