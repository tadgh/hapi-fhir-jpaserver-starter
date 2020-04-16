package ca.uhn.fhir.jpa.starter.interceptors;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.starter.models.QuestionnaireResponseFlattened;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.interceptor.Interceptor;

import static org.slf4j.LoggerFactory.getLogger;

@Interceptor
public class QuestionnaireResponseElasticEmitterInterceptor {
  private static final Logger ourLog = getLogger(QuestionnaireResponseElasticEmitterInterceptor.class);

  @Autowired
  FhirContext myContext;

  @Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
  public void flattenAndLogQuestionnaireResponse(IBaseResource theResource, ServletRequestDetails theServletRequestDetails) {
    if (isSelfAssessment(theResource)) {
      ObjectMapper mapper = new ObjectMapper();
      QuestionnaireResponseFlattened questionnaireResponseFlattened = new QuestionnaireResponseFlattened((QuestionnaireResponse) theResource);

      try {
        ourLog.info("AUDIT:{} ", mapper.writeValueAsString(questionnaireResponseFlattened));
      } catch (JsonProcessingException theE) {
        theE.printStackTrace();
      }
    }
  }

  private boolean isSelfAssessment(IBaseResource theResource) {
    return theResource instanceof QuestionnaireResponse && SELF_ASSESSMENT_IDENTIFIER.equalsIgnoreCase(((QuestionnaireResponse)theResource).getIdentifier().getValue());
  }

}
