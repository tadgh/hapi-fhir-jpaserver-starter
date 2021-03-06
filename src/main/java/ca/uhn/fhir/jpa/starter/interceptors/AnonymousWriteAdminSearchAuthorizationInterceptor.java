package ca.uhn.fhir.jpa.starter.interceptors;


import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Value;

import javax.interceptor.Interceptor;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Interceptor
public class AnonymousWriteAdminSearchAuthorizationInterceptor extends AuthorizationInterceptor {

  //This can be injected with -Dadmin.token as a system property.
  //If omitted, disables the admin token.
  @Value("${admin.token:}")
  private String myAdminBearerToken;

  @Value("${clinician.token:}")
  private String myClinicianToken;


  @Override
  public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {

    String authHeader = theRequestDetails.getHeader("Authorization");
    if (!StringUtils.isBlank(myAdminBearerToken) && bearerMatches(authHeader, myAdminBearerToken)){
      return new RuleBuilder().allowAll().build();
    } else if (!StringUtils.isBlank(myClinicianToken) && bearerMatches(authHeader, myClinicianToken)) {
      return new RuleBuilder()
        .allow().write().resourcesOfType(QuestionnaireResponse.class).withAnyId().andThen()
        .allow().read().resourcesOfType(QuestionnaireResponse.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Questionnaire.class).withAnyId().andThen()
        .allow().write().resourcesOfType(CareTeam.class).withAnyId().andThen()
        .allow().read().resourcesOfType(CareTeam.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ValueSet.class).withAnyId().andThen()
        .allow().write().resourcesOfType(Patient.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
        .allow().write().resourcesOfType(Practitioner.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Practitioner.class).withAnyId().andThen()
        .allow().write().resourcesOfType(Encounter.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Encounter.class).withAnyId().andThen()
        .allow().write().resourcesOfType(EpisodeOfCare.class).withAnyId().andThen()
        .allow().read().resourcesOfType(EpisodeOfCare.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
        .denyAll()
        .build();
    } else {
      return new RuleBuilder()
        .allow().write().resourcesOfType(QuestionnaireResponse.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Questionnaire.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ValueSet.class).withAnyId().andThen()
        .allow().write().resourcesOfType(Patient.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Patient.class).withAnyId().andThen()
        .allow().write().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ServiceRequest.class).withAnyId().andThen()
        .denyAll()
        .build();
    }
  }

  private boolean bearerMatches(String theBearer, String theKnownToken) {
    return ("Bearer " + theKnownToken).equals(theBearer);
  }
}
