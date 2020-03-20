package ca.uhn.fhir.jpa.starter.interceptors;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;

import javax.interceptor.Interceptor;


@Interceptor
public class MetadataCollectingInterceptor {
  public static final String IP_EXTENSION_URL = "https://smilecdr.com/extensions/source-ip-address";

  @Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
  public void addIPInformationToResource(IBaseResource theResource, ServletRequestDetails theServletRequestDetails) {
    if (theResource instanceof QuestionnaireResponse) {
      ((QuestionnaireResponse) theResource).addExtension(IP_EXTENSION_URL, new StringType(theServletRequestDetails.getServletRequest().getRemoteAddr()));
    }
  }

}
