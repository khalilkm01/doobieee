package Main

import com.twitter.finagle.http.{Request,Response}
import com.twitter.util.Future
import wvlet.airframe.http.{Endpoint, HttpMethod, HttpRequest}


trait MyApi{

    @Endpoint(method = HttpMethod.GET, path = "/admin/getRooms")
    def getRooms:List[Room] = Admin.getRooms
}