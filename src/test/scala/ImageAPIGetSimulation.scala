import java.nio.charset.StandardCharsets
import java.util.Base64

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import no.ndla.network.NdlaClient

import scalaj.http.Http

class ImageAPIGetSimulation extends Simulation with NdlaClient {

  val ndlaClient = new NdlaClient()

  val tokenResponse = ndlaClient.fetch[TokenResponse](Http("https://test.api.ndla.no/auth/tokens").postForm(Seq("grant_type" -> "client_credentials")).headers(Seq(("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"), ("Authorization", "Basic " + Base64.getEncoder.encodeToString("YOURCLIENTID:YOURCLIENTSECRET".getBytes(StandardCharsets.UTF_8))))))
  val token = tokenResponse.get.accessToken

  val httpConf = http
    .baseURL("https://test.api.ndla.no/image-api/v1")

  val scn = scenario("BasicSimulation").repeat(200) {
    exec(http("request_1")
      .get("/images/?page-size=100").headers(Map("Authorization" -> s"Bearer $token")))
  }

  setUp(
    scn.inject(atOnceUsers(500))
  ).protocols(httpConf)
}
