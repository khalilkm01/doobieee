package Main

import RoomsDB._

object Admin extends AdminControls{
    def getRooms: List[Room] = RoomsDB.getAvailableRooms ::: RoomsDB.getBookedRooms
    def getAvailableRooms: List[Room] = RoomsDB.getAvailableRooms
    def getBookedRooms: List[Room] = RoomsDB.getBookedRooms
    def checkRoomTenant(roomID: Int): Option[Person] = RoomsDB.checkRoomTenant(roomID)
    def addRoom(room: Room): Boolean = RoomsDB.addRoom(room)
    def removeRoom(roomID: Int): Boolean = RoomsDB.removeRoom(roomID)
    def addPerson(person: Person): Boolean = RoomsDB.addPerson(person)
    def removePerson(id: Int): Boolean = RoomsDB.removePerson(id)
}