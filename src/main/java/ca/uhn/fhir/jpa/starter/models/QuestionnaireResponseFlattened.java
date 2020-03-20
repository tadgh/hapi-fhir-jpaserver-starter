package ca.uhn.fhir.jpa.starter.models;

import ca.uhn.fhir.jpa.starter.interceptors.MetadataCollectingInterceptor;
import ca.uhn.fhir.jpa.starter.interceptors.QuestionnaireResponseElasticEmitterInterceptor;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Type;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class QuestionnaireResponseFlattened {
  @JsonProperty
  String ipAddress;

  @JsonProperty
  Map<String, String> answers;

  @JsonProperty
  String cookie;

  @JsonProperty
  @JsonSerialize(using = JsonDateSerializer.class)
  Date creationDate;

  public QuestionnaireResponseFlattened(QuestionnaireResponse theQuestionnaireResponse) {
    this.ipAddress = theQuestionnaireResponse.getExtensionByUrl(MetadataCollectingInterceptor.IP_EXTENSION_URL).getValue().toString();
    //this.cookie = theQuestionnaireResponse.getExtensionByUrl(MetadataCollectingInterceptor.COOKIE_URL).getValue().toString();
    this.creationDate = theQuestionnaireResponse.getAuthored();
    this.answers = buildQuestionAndAnswerMap(theQuestionnaireResponse);
  }

  private Map<String, String> buildQuestionAndAnswerMap(QuestionnaireResponse theQuestionnaireResponse) {
    return theQuestionnaireResponse.getItem()
      .stream()
      .collect(Collectors
        .toMap(item -> item.getLinkId(), item -> convertAnswerToString(item.getAnswerFirstRep())));
  }

  private String convertAnswerToString(QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent theAnswerFirstRep) {
    Type answer = theAnswerFirstRep.getValue();
    return answer.toString();
  }
}
