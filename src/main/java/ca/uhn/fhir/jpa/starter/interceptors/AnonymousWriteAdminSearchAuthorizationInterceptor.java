package ca.uhn.fhir.jpa.starter.interceptors;


import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.ValueSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.interceptor.Interceptor;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Interceptor
public class AnonymousWriteAdminSearchAuthorizationInterceptor extends AuthorizationInterceptor {

  //This can be injected with -Dadmin.token as a system property.
  //If omitted, disables the admin token.
  @Value("${admin.token:}")
  private String myAdminBearerToken;

  @Override
  public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {

    String authHeader = theRequestDetails.getHeader("Authorization");
    if (!StringUtils.isBlank(myAdminBearerToken) && ("Bearer "+ myAdminBearerToken).equals(authHeader)) {
      return new RuleBuilder().allowAll().build();
    } else {
      return new RuleBuilder()
        .allow().write().resourcesOfType(QuestionnaireResponse.class).withAnyId().andThen()
        .allow().read().resourcesOfType(Questionnaire.class).withAnyId().andThen()
        .allow().read().resourcesOfType(ValueSet.class).withAnyId().andThen()
        .denyAll()
        .build();
    }
  }
}
