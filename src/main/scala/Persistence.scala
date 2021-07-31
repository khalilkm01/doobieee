package Main
import doobie._
import doobie.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext


//DATABASE commands  AND 

object RoomsDB extends DB with DBCONFIG{
    val trueval: Boolean = true
    val falseval: Boolean = false

    def roomExists(roomID: Int): Option[Room] = { // Returns a Room if it exists or None otherwise
        sql"""SELECT * FROM rooms WHERE RoomID = $roomID"""
        .query[Room].option
        .transact(xa).unsafeRunSync
    }

    def checkInRoom(roomID: Int, personID: Int): Boolean = {//If return value is false then there is an error

        sql"""UPDATE rooms SET occupantid = $personID WHERE roomid = $roomID""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync

        sql"""UPDATE rooms SET occupied = $trueval WHERE roomid = $roomID""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync

        

        roomExists(roomID) match {
            case Some(Room(id, occupied, occupantID)) => if (occupied) true else false
            case _ => false
        }
    }
    
    def checkOutRoom(roomID: Int): Boolean = {//If the return value is false then there is an error

        sql"""UPDATE rooms SET occupied = False WHERE roomid = ${roomID}""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync

        sql"""UPDATE rooms SET occupantid = NULL WHERE roomid = $roomID""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync

        roomExists(roomID) match {
            case Some(Room(id, occupied, occupantID)) => if (occupied) false else true
            case _ => false
        }
    }
    
    def getBookedRooms: List[Room] = {// Returns a list of all Booked Rooms
        sql"""SELECT * FROM rooms WHERE occupied = $trueval"""
         .stripMargin
         .query[Room].stream
         .compile.toList
         .transact(xa).unsafeRunSync
    }

    def getAvailableRooms: List[Room] = {//Returns a list of all VAailable rooms
        sql"""SELECT * FROM rooms WHERE occupied = $falseval"""
         .stripMargin
         .query[Room].stream
         .compile.toList
         .transact(xa).unsafeRunSync
    }

    def checkRoomOccupied(roomID: Int): Boolean = {//Returns Boolean stating whether room is occupied
        roomExists(roomID) match {
            case Some(room) => if (room.occupied) true else false
            case _ => false
        }
        
    }

    def checkRoomTenant(roomID: Int): Option[Person] = {//Either returns none or value from PersonExists function
        
        checkRoomOccupied(roomID) match{
            case true => {
                val room: Option[Room] = roomExists(roomID)
                room match {
                    case Some(Room(id, occupied, occupantID)) => {
                        occupantID match {
                            case Some(theOccupantID) => personExists(theOccupantID)
                            case None => None
                        }
                    }
                    case _ => None
                }
            }
            case false => None
        }
    }

    def addRoom(room: Room): Boolean = {// Returns a Boolean indicating success
        room match {
            case Room(id, occupied, occupantID) => {
                sql"""INSERT into rooms (roomid, occupied, occupantid) 
                VALUES (${id}, ${occupied}, ${occupantID})"""
                .stripMargin
                .update.run
                .transact(xa).unsafeRunSync

                roomExists(room.id) match {
                    case Some(Room(id, occupied, occupantID)) => true
                    case _ => false
                }
            }
        }       
       
    }

    def removeRoom(roomID: Int): Boolean ={// Returns a boolean indicating success
        sql"""DELETE FROM rooms WHERE roomid = ${roomID}""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync
        roomExists(roomID) match {
                    case None => true
                    case _ => false
        }

    }

    def personExists(personID: Int): Option[Person] = {// Returns None or Person
        sql"""SELECT * FROM person WHERE id = ${personID}"""
         .stripMargin
         .query[Person].option
         .transact(xa).unsafeRunSync
    }

    def addPerson(person: Person): Boolean = {//Returns a boolean indicating success
        person match { 
            case Person (id, firstName, lastName, gender, dateOfBirth, email) => {
                sql"""INSERT into person (firstName, lastName, gender, dateOfBirth, email) 
                VALUES (${firstName}, ${lastName}, ${gender}, ${dateOfBirth}, ${email})"""
                .stripMargin
                .update.run
                .transact(xa).unsafeRunSync
            }

        }
        personExists(person.id) match{
            case Some(Person (id, firstName, lastName, gender, dateOfBirth, email)) => true
            case None => false
        }
    }

    def removePerson(id: Int): Boolean = {// Returns a boolean indicating success
        sql"""DELETE FROM person WHERE id = ${id}""".stripMargin
        .update.run
        .transact(xa).unsafeRunSync
        personExists(id) match{
            case Some(Person (id, firstName, lastName, gender, dateOfBirth, email)) => false
            case None => true
        }
    }

}



//DB Boiler Plate Code


    //Query0[Person]
      
// 
//val jans: ConnectionIO[List[Person]] = firstNameSearch("Jan") //List[Person]

// firstNameSearch("Natalee")
// .stream           //Stream[ConnectionIO, Person]
// .take(10)         //Stream[ConnectionIO, Person]
// .compile.toList   //ConnectionIO[List[Person]]
// .transact(xa).unsafeRunSync.foreach(println)


// sql"""SELECT * FROM person
//       WHERE firstName  = 'Natalee'  """.stripMargin
//   .query[Person]    //Query0[Person]
//   .stream           //Stream[ConnectionIO, Person]
//   .take(10)         //Stream[ConnectionIO, Person]
//   .compile.toList   //ConnectionIO[List[Person]]
//   .transact(xa)     //IO[List[Person]]
//   .unsafeRunSync()  //List[Person]
//   .foreach(println)


  /*
  Lessons Here:
    *Inside the Query Define how the result set should be retunred
    *THe toList method can be swapped with: 
      -Option: Just means an option will be returned, raises an exception if more than one row returned
      -Unique: Means a single value will be retunred, raising an exception if otherwise
      -Nel: Retunes a non empty list, rasing an exception if otherwise 
  */
