package community.flock.facedetection.controller

import com.amazonaws.services.rekognition.AmazonRekognition
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder
import com.amazonaws.services.rekognition.model.DetectFacesRequest
import com.amazonaws.services.rekognition.model.DetectFacesResult
import com.amazonaws.services.rekognition.model.Image
import com.amazonaws.services.rekognition.model.S3Object
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*


@RestController
//@EnableWebMvc
@RequestMapping(value = ["/face-detection"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FaceFinderController {

    private val log = LoggerFactory.getLogger(javaClass)
    private val rekognitionClient: AmazonRekognition = AmazonRekognitionClientBuilder.defaultClient()


    @GetMapping("ping")
    fun ping(): String {
        return "You got me"
    }

    @PostMapping("/identify")
    fun identifyFace(@RequestBody faceIdentityRequest: FaceIdentityRequest): FaceIdentityResponse {
        log.debug("Identifying face")
        log.info(faceIdentityRequest?.toString())
        val image = getDefaultImage()
        val detectFacesRequest = DetectFacesRequest().withImage(image)
        val detectFaces = rekognitionClient.detectFaces(detectFacesRequest)

        return FaceIdentityResponse(
                detectedFaces = detectFaces,
                requestedImage = image
        )
    }

    private fun getDefaultImage() = Image()
            .withS3Object(
                    S3Object()
                            .withBucket("flock-face-detection-2")
                            .withName("4041699.jpeg")
            )
}

data class FaceIdentityResponse(
        val detectedFaces: DetectFacesResult,
        val requestedImage: Image
)


data class FaceIdentityRequest(
        var name: String
)
