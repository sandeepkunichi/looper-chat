import javax.inject._

import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

/**
  * Created by Sandeep.K on 19-08-2017.
  */
class Filters @Inject() (corsFilter: CORSFilter) extends DefaultHttpFilters(corsFilter)
