/*import play.api.mvc._
import play.api.libs.iteratee.Iteratee



class LoggingFilter extends EssentialFilter {

  def apply(next: EssentialAction) = new EssentialAction {
    def apply(request: RequestHeader) = {
      if (request.path.startsWith("/tacosAdminManagement") && request.session.get("connected").isEmpty) {
        Iteratee.ignore[Array[Byte]].map(_ => Results.Forbidden())
      } else {
        next(request)
      }
    }
  }

}*/