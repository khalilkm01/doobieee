package Main

import wvlet.airframe._
import wvlet.airframe.http._
import com.twitter.io.{Buf,Reader}
import com.twitter.finagle.Http
import com.twitter.finagle.http.Request
import com.twitter.util.Duration
import wvlet.airframe.http.finagle.{Finagle, FinagleSyncClient}
import wvlet.airframe.http.{Endpoint, HttpMethod, Router}

// Define API routes. This will read all @Endpoint annotations in MyApi
// You can add more routes by using `.add[X]` method.

object Interface extends App{


    trait AdminApi{
        @Endpoint(method = HttpMethod.GET, path = "/admin/getRooms")
        def getRooms:Reader[Room] = Reader.fromSeq(Admin.getRooms)


    }

   trait StaffApi{
       @Endpoint(method = HttpMethod.GET, path = "/staff/seeAvailability")
       def seeAvailability: List[Room] = Staff.seeAvailability

       @Endpoint(method = HttpMethod.POST, path = "/staff/")
   }



    val router = Router.add[AdminApi].add[StaffApi]

    Finagle.server
    .withPort(8080)
    .withRouter(router)
    .start { server =>
        // Finagle http server will start here
        // To keep running the server, run `server.waitServerTermination`:
        server.waitServerTermination
    }
// The server will terminate here
}