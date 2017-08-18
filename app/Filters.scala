import javax.inject._

import play.api._
import play.api.http.HttpFilters

/**
  * Created by Sandeep.K on 19-08-2017.
  */
@Singleton
class Filters @Inject() (env: Environment) extends HttpFilters {

  override val filters = {
    Seq.empty
  }

}
