package Main
import doobie._
import doobie.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext
import java.util.Date

//Interface Structures
case class Person (id: Int, firstName: String, lastName: String, gender: String, dateOfBirth: Date, email: Option[String])
case class Room (id: Int, occupied: Boolean, occupantID: Option[Int])

//DB Initialization and Configuration
trait DBCONFIG{
    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val driver: String =  "org.postgresql.Driver"
    val url: String = "jdbc:postgresql://localhost:5431/test"
    val user: String =  "postgres"
    val password: String = ""

    val xa = Transactor.fromDriverManager[IO](
    driver,   // driver classname
    url,      // connect URL (driver-specific)
    user, // just for testing
    password
    )
}

//Authentication for Admin?//
////////////////////////////

//Database Functions
abstract class DB {
    //Functions to do with Room
    def roomExists(roomID: Int): Option[Room]
    def checkInRoom(roomID: Int, personID: Int): Boolean
    def checkOutRoom(roomID: Int): Boolean 
    def getBookedRooms: List[Room]
    def getAvailableRooms: List[Room]
    def checkRoomOccupied(roomID: Int): Boolean
    def addRoom(room: Room): Boolean
    def removeRoom(roomID: Int): Boolean
    

    //Functions to do with Person
    def personExists(personID: Int): Option[Person]
    def checkRoomTenant(roomID: Int): Option[Person]
    def addPerson(person: Person): Boolean
    def removePerson(id: Int): Boolean
}


//Staff control Functions
//Check In and Out individuals from their rooms
//Register new individual
//See Available rooms


abstract class StaffControls {
    def checkIn(roomID: Int, personID: Int): Boolean
    def checkOut(roomID: Int): Boolean 
    def registerPerson(person: Person): Boolean
    def seeAvailability: List[Room]
}


//Admin control Functions
//Get all rooms and respective info
//See Booked rooms and Available rooms
//Check a rooms tenant
//add and remove room
//add and remove person

abstract class AdminControls {
    def getRooms: List[Room]
    def getAvailableRooms: List[Room]
    def getBookedRooms: List[Room]
    def checkRoomTenant(roomID: Int): Option[Person] 
    def addRoom(room: Room): Boolean
    def removeRoom(roomID: Int): Boolean
    def addPerson(person: Person): Boolean
    def removePerson(id: Int): Boolean
}







