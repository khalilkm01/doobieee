package Main


object Staff extends StaffControls {
    def checkIn(roomID: Int, personID: Int): Boolean = RoomsDB.checkInRoom(roomID, personID)
    def checkOut(roomID: Int): Boolean  = RoomsDB.checkOutRoom(roomID)
    def registerPerson(person: Person): Boolean = RoomsDB.addPerson(person)
    def seeAvailability: List[Room] = RoomsDB.getAvailableRooms
}







