package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.*;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.UpdatePageStep;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.util.List;
import java.util.stream.Collectors;

public class UpdatePageStepExecution extends AbstractStepExecution<PageCreated, UpdatePageStep> {
    private static final long serialVersionUID = 6330386183041962984L;

    public UpdatePageStepExecution(UpdatePageStep updatePageStep, StepContext context, ConfluenceSite confluenceSite) {
        super(updatePageStep, context, confluenceSite);
    }

    @Override
    public void validate(final UpdatePageStep step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type CreatePageStep is null");
        }

        if (step.getSite() == null) {
            throw new IllegalStateException("Given site is null");
        }

        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalStateException("The title of the page is null or empty");
        }
    }

    @Override
    protected PageCreated run() throws Exception {
        try {
            return getService(ContentService.class).updatePage(createUpdatedPage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Page createUpdatedPage() {
        String pageId = extractPageId(getStep().getTitle());
        Page oldPage = requestOldPageInformation(pageId);
        Page newPage = new Page();

        newPage.setTitle(getStep().getTitle());
        newPage.setType("page");
        newPage.setStatus("current");
        newPage.setId(pageId);

        Version version = new Version();
        version.setNumber(oldPage.getVersion().getNumber() + 1);
        newPage.setVersion(version);

        Space space = new Space();
        space.setKey(oldPage.getSpace().getKey());
        newPage.setSpace(space);

        Body body = new Body();
        Storage storage = new Storage();

        if (!getStep().isAppend()) {
            storage.setValue(getStep().getNewContent());
        } else {
            oldPage = requestOldPageContent(pageId);
            storage.setValue(
                    oldPage.getBody().getStorage().getValue()
                            + getStep().getNewContent()
            );
        }
        storage.setRepresentation("storage");

        body.setStorage(storage);
        newPage.setBody(body);
        newPage.clearUnmappedFields();

        return newPage;
    }

    private String extractPageId(final String title) {
        ContentService service = getService(ContentService.class);
        Content content = service.getContent();
        List<Result> resultList = content.getResults().stream()
                .filter(s -> title.equals(s.getTitle()))
                .collect(Collectors.toList());

        if (resultList.isEmpty()) {
            throw new IllegalStateException(String.format("No page with title \"%s\" found", title));
        } else if (resultList.size() > 1) {
            throw new IllegalStateException(String.format("Multiple pages with title \"%s\" found", title));
        }

        return String.valueOf(resultList.get(0).getId());
    }

    private Page requestOldPageContent(final String id) {
        ContentService service = getService(ContentService.class);
        return service.getPageBodyById(id);
    }

    private Page requestOldPageInformation(final String id) {
        ContentService service = getService(ContentService.class);
        return service.getPageById(id);
    }
}
