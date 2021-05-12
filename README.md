# Hospital Database Project
## 1 Introduction
In this project, we will model and build a hospital database management system. We will manage
this system to track information about different hospitals, such as the specialized departments
they maintain, the maintenance of these departments, the doctors and staff and the appointments
attended by the doctors, the medical records of the patients who took the appointments.
The project consists of three phases: (i) Requirement Analysis using the ER-model design, (ii)
Translation to Relational schema, and (iii) Implementation.
Projects should be done in teams of TWO students. The project members must be within the
same lab (morning or noon). Choose your partner wisely because the final evaluation is based
on the group performance! In your report (for each phase), explicitly enumerate the tasks that
each member of your group was responsible for, and how you collaborated. If one of the group
members does most of the work, the grade will be proportional to the effort. You can also use the
piazza room to find your partner. If you are not able to find a partner, one will be assigned to you
at random. Please email the TA and the Professor immediately if you need help finding a partner.
We will share a worksheet with you (in iLearn) where you can submit the name of your team
members before April 25th, and after that, we will let you know the group numbers. Please
rename your compressed file (.zip format) of each phase with your group number and name of
team members. For example, x and y are the first names of Group z, the name of the compressed
file will be “Group_z_x_y.zip”.
## Phase 1: ER Design
For the first phase, you are given a set of requirements for your database that appear in Section 2
of this document. You need to design a logical model of your database using the ER model. Note
that we are giving you a set of requirements, and not the complete set of attributes each entity set
will hold. From the description, you must first identify the entity sets to be included in your
model, and the attributes for each entity set that will enable you to answer the given queries.
Following these guidelines, you should generate an ER diagram using your favorite diagram
editing software. Use only the basic ER model that includes entities, relationships, and attributes.
Do not forget to indicate any key and participation constraints. Also, make sure to include
additional documentation describing the assumptions that you made during the design process.
You have to submit all your files via iLearn on the due date. Your submission must be a single
zip file. Make sure to check that everything is included in your submission and it can be
uncompressed without any errors.
You can make reasonable assumptions on your design, as long as:
● you state them clearly in the documentation for this phase, and,
● they do not contradict the system requirements analysis we provide.
The due date for this phase is: May 12th, Wednesday at 11:59 PM
Phase 2: Translation to Relational Schema
In this phase, we will provide you with an ER diagram that is a solution to Phase 1 (so that the
whole class will proceed with the same design). This final ER diagram will be the starting point
for the second phase, which involves the creation of the relational schema.
Your task in this phase will be to translate the provided ER design to a PostgreSQL relational
database schema. The database schema will be in a form of a single executable SQL script (*.sql
file with SQL statements). You must submit this SQL script via iLearn on the due date. The
SQL script must include the necessary DROP statements at the beginning so it is easy to test.
Check how the drop statement works and consider using the IF EXISTS statements where
necessary.
In this phase, you will be evaluated for the correctness and completeness of your relational
schema. You may find some constraint in the model and/or system requirement analysis that is
not possible to represent or enforce in the relational schema. You may specify all these issues in
the documentation for this phase. Your submission must be a single compressed file, containing
all the aforementioned files.
The due date of this phase is: May 19th, Wednesday at 11:59 PM
## Phase 3: Implementation
Your tasks in this phase will be:
● Develop a client application using the Java Database Connector (JDBC) for PSQL.
● Use the client application to support specific functionality and queries for your online
booking system.
In this phase, we will provide you with a create.sql script that recreates the relational schema of
phase 2. You will use this schema to test and demo your project submission to us. Additionally,
we will give you a collection of .csv files containing dummy data that are compatible with the
provided relational schema. You will have to create your own .sql scripts to insert the data from
the given .csv files into the database.
Finally, we will give you a skeleton code for the client application. The code will be in Java and
contain some basic functionality that will help you communicate with the database and issue
various .sql statements. You should not worry if you are unfamiliar with Java since the skeleton
is pretty straightforward.
This phase of the project is challenging, therefore we advise you to start early and allocate at
least 25 hours per person to get it finished. Make sure to consider all possible scenarios for the
client application and try to handle any exceptions that arise during the regular operation of your
application.
For this phase, you will be evaluated based on the system requirements. Your GUI and source
code will also be taken into consideration in your final evaluation. Groups that implement
systems with user-friendly interfaces, extra functionalities will receive extra credit. A final report
about your system along with its source code has to be sent to your TA before the due date. You
have to submit the documentation and final source code on iLearn.
The due date of this phase is: June 11th, Friday at 11:59 PM
## 1.1 Grading
Your contribution to this project will be graded based on the following characteristics:
● Phase 1: (30%)
- Conceptual Design (ER Diagram)
- You must submit your solutions on iLearn.
● Phase 2: (10%)
- Logical DB Design (Relational Database Schema)
- You must submit your solutions on iLearn.
● Phase 3: (60%)
- Documentation of the project including details about your assumptions (10%).
- Implementation of SQL queries in the Client Application (30%).
- Physical DB Design (DB performance tuning indexes) (20%).
- You must submit the documentation and source code in a zip file on iLearn and
send the zip file to the TA.
- Extra credit for good GUI design and interface, any dataset or schema
changes/extensions, etc. (20%).
## 2 Requirement Analysis
You will design a hospital database management system that serves the needs of hospital
managers, doctors, and patients. Each of these types of individuals needs access to the following
information:
#### Hospital Management:
1. Given a department ID, get the active appointments for the week.
2. Given a department ID and a date, find (i) the available appointments, (ii) the active
appointments, (iii) the list of patients who made appointments on that given date.
3. Given a doctor name and a date, list all the available appointments of the doctor for the
given date.
4. Given a patient ID, retrieve the patient details (Name, Age, Gender, Address, Total
number of appointments to the hospital, etc.).
5. Given a department ID and a patient ID, list all the appointments (active or past) made by
the patient in that department.
6. Given a maintenance staff ID, list all the requests addressed by the staff.
*N.B.: Active appointments are already booked by the patients, Past appointments are already
attended by the patients, and Available appointments can be booked by patients.*
### Hospital Maintenance Staff:
1. Given a date range, list all the appointments (past or active) made by a given patient ID.
2. Given a doctor name, list all maintenance requests made by the doctor.
3. After a maintenance request is addressed, make necessary entries showing the available
appointments for that doctor of the department.
### Patients:
1. Given a hospital name, find the specialized departments in the hospital.
2. Given a hospital name and a specialized department name, find all the doctors whose
appointments are available on the week.
3. Given an appointment number, find the appointment details (time slot, doctor name,
department, etc.)
4. Make an appointment for a doctor of a department:
- Get on the waitlist for an appointment if the chosen doctor has no available slots
in the week.
### Doctors:
1. Make a maintenance request with a list of available appointments (time slot, department
name, number of patients per hour, etc.).
