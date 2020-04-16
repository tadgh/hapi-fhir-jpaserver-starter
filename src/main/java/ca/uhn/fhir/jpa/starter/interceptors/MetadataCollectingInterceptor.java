package ca.uhn.fhir.jpa.starter.interceptors;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.StringType;

import javax.interceptor.Interceptor;

import static ca.uhn.fhir.jpa.starter.Constants.IP_EXTENSION_URL;


@Interceptor
public class MetadataCollectingInterceptor {

  @Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
  public void addIPInformationToResource(IBaseResource theResource, ServletRequestDetails theServletRequestDetails) {
    if (theResource instanceof QuestionnaireResponse) {
      ((QuestionnaireResponse) theResource).addExtension(IP_EXTENSION_URL, new StringType(theServletRequestDetails.getServletRequest().getRemoteAddr()));
    }
  }

}
