package ca.uhn.fhir.jpa.starter.models;

import ca.uhn.fhir.jpa.starter.interceptors.MetadataCollectingInterceptor;
import ca.uhn.fhir.jpa.starter.interceptors.QuestionnaireResponseElasticEmitterInterceptor;
import ca.uhn.fhir.jpa.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hl7.fhir.r4.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class QuestionnaireResponseFlattened {

  private static final List<String> outcomeLinkIds = new ArrayList<>();
  static {
    outcomeLinkIds.add("contact_healthcare_provider");
    outcomeLinkIds.add("contact_healthcare_provider_for_reassessment");
    outcomeLinkIds.add("seek_clinical_assessment");
    outcomeLinkIds.add("illness_advise");
  }
  @JsonProperty
  String ipAddress;

  @JsonProperty
  Map<String, Boolean> answers;

  @JsonProperty
  Map<String, String> extensions;

  @JsonProperty
  String clinicalOutcome;

  @JsonProperty
  @JsonSerialize(using = JsonDateSerializer.class)
  Date creationDate;

  public QuestionnaireResponseFlattened(QuestionnaireResponse theQuestionnaireResponse) {
    this.ipAddress = theQuestionnaireResponse.getExtensionByUrl(MetadataCollectingInterceptor.IP_EXTENSION_URL).getValue().toString();
    this.creationDate = theQuestionnaireResponse.getAuthored();
    this.answers = buildQuestionAndAnswerMap(theQuestionnaireResponse);
    this.extensions = buildExtensionsMap(theQuestionnaireResponse);
    this.clinicalOutcome = determineOutcome(theQuestionnaireResponse);
  }

  private String determineOutcome(QuestionnaireResponse theQuestionnaireResponse) {
    return theQuestionnaireResponse.getItem().stream()
      .filter(i -> outcomeLinkIds.contains(i.getLinkId()))
      .map(QuestionnaireResponse.QuestionnaireResponseItemComponent::getLinkId)
      .findFirst().orElse("no_outcome");
  }

  private Map<String, String> buildExtensionsMap(QuestionnaireResponse theQuestionnaireResponse) {
    return theQuestionnaireResponse.getExtension()
      .stream()
      .collect(Collectors.toMap(
        Extension::getUrl,
        item -> item.getValueAsPrimitive().toString()
      ));
  }

  private Map<String, Boolean> buildQuestionAndAnswerMap(QuestionnaireResponse theQuestionnaireResponse) {
    return theQuestionnaireResponse.getItem()
      .stream()
      .filter(q -> q.getAnswerFirstRep().getValue().isBooleanPrimitive())
      .collect(Collectors
        .toMap(QuestionnaireResponse.QuestionnaireResponseItemComponent::getLinkId,
          item -> ((BooleanType)item.getAnswerFirstRep().getValue()).booleanValue()));
  }
}
