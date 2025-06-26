*** Prerequisites ***
-Java 17+

-Maven (optional)

% Running the App
Option 1: Use Maven

If Maven is installed:

mvn clean compile
mvn exec:java -Dexec.mainClass="com.chronoscouriers.DispatchApp"


Option 2: Use an IDE 

Open the project in IntelliJ IDEA.

Let it import the pom.xml.

Open the DispatchApp.java file.

Click the green run arrow next to the main() method.

Done! The app will launch in your console.

% Sample Test Case:
Try this sequence:

 1.Add a rider

ADD_RIDER R1 Rahul FRAGILE,HEAVY
✅ Adds rider R1 named Rahul, capable of carrying fragile and heavy packages.

 2️. Place an order

PLACE_ORDER P1 EXPRESS 1735689600000 MG_Road_Bangalore 2.5 FRAGILE
✅ Adds an EXPRESS order:

Deadline: 1735689600000

Destination: MG_Road_Bangalore

Weight: 2.5kg

Requirement: FRAGILE

✅ 3. Mark the rider as available

UPDATE_RIDER_STATUS R1 AVAILABLE
✅ 4️. Dispatch

DISPATCH
✅ Will try to match available rider(s) with pending order(s).

Should print:

>>> Assignment Created: ASN-XXXXXXX for Package P1 -> Rider R1
 5️. Get rider status

GET_RIDER_STATUS R1
✅ Shows the status of rider R1 (should be ON_DELIVERY) and the assignment.

✅ 6️. Complete the delivery

COMPLETE_DELIVERY ASN-XXXXXXX
✅ Marks the assignment as completed.
Rider status ➔ AVAILABLE
Package status ➔ DELIVERED

7️. Get delivered packages for the rider

GET_RIDER_DELIVERED R1
✅ Lists deliveries completed by rider R1 in the last 24h.

 8️. Get missed express deliveries

GET_MISSED_EXPRESS
✅ Shows any express packages that missed their deadline.

 9.Exit the app

EXIT
