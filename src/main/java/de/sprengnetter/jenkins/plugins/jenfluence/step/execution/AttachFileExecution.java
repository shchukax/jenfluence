package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.AttachFileStep;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AttachFileExecution extends AbstractStepExecution<Boolean, AttachFileStep> {

    private final ConfluenceSite confluenceSite;

    public AttachFileExecution(AttachFileStep step, StepContext context, ConfluenceSite confluenceSite) {
        super(step, context, confluenceSite);
        this.confluenceSite = confluenceSite;
    }

    @Override
    public void validate(AttachFileStep step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type \"AttachFileStep\" is null");
        }
    }

    @Override
    protected Boolean run() throws Exception {
        ContentService service = getService(ContentService.class);
//        MultipartFormDataOutput formData = new MultipartFormDataOutput();
//        formData.addFormData("file", new File(getStep().getFilePath()), MediaType.TEXT_PLAIN_TYPE);
//        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(formData, MultipartFormDataOutput.class);
        String id = "1146884";

        MultipartOutput output = new MultipartOutput();
        output.addPart(new File(getStep().getFilePath()), MediaType.TEXT_PLAIN_TYPE);

        service.attachFile(id, output);
        return Boolean.FALSE;
    }
}