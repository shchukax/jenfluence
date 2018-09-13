package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.*;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.UpdatePageStep;
import org.jenkinsci.plugins.workflow.steps.StepContext;

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

        if (step.getSpaceKey() == null || step.getSpaceKey().isEmpty()) {
            throw new IllegalStateException("The title of the page is null or empty");
        }

        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalStateException("The title of the page is null or empty");
        }
    }

    @Override
    protected PageCreated run() {
        try {
            return getService(ContentService.class).updatePage(buildUpdatedPage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Page buildUpdatedPage() {
        //Get information about the page to update
        String pageId = extractPageId(getStep().getSpaceKey(), getStep().getTitle());
        Page oldPage = requestOldPageInformation(pageId);

        //Set page base information
        Page newPage = new Page();

        newPage.setType(Page.TYPE);
        newPage.setStatus(Page.STATUS);
        newPage.setId(pageId);

        String newTitle = getStep().getNewTitle();
        newPage.setTitle(newTitle == null || newTitle.trim().length() == 0 ? getStep().getTitle() : newTitle);

        //Get the new Version number and increase it by 1
        Version version = new Version();
        version.setNumber(oldPage.getVersion().getNumber() + 1);
        newPage.setVersion(version);

        //Set the space information
        Space space = new Space();
        space.setKey(oldPage.getSpace().getKey());
        newPage.setSpace(space);

        //Set the new content for the page
        Body body = new Body();
        body.setStorage(buildUpdatedPageStorage(pageId));
        newPage.setBody(body);

        return newPage;
    }

    private String extractPageId(final String spaceKey, final String title) {
        ContentService service = getService(ContentService.class);
        Content content = service.getPage(spaceKey, title, null);

        if (content.getResults().size() == 0 || content.getResults().get(0).getId() == null) {
            throw new IllegalStateException("No page with title " + getStep().getTitle() + " in space with key "
                    + getStep().getSpaceKey() + " was found");
        }

        /*
         * Should NEVER happen, because Confluence does not allow multiple pages with the same name in a space.
         * But you never know which bugs are present in Confluence or which new features are coming in future versions!
         * Better safe than sorry.
         */
        if (content.getResults().size() > 1) {
            throw new IllegalStateException("Multiple possible pages with title " + getStep().getTitle()
                    + "in space with key " + getStep().getSpaceKey() + " were found");
        }

        return String.valueOf(content.getResults().get(0).getId());
    }

    private Page requestOldPageContent(final String id) {
        ContentService service = getService(ContentService.class);
        return service.getPageBodyById(id);
    }

    private Page requestOldPageInformation(final String id) {
        ContentService service = getService(ContentService.class);
        return service.getPageById(id);
    }

    private Storage buildUpdatedPageStorage(final String pageId) {
        Storage storage = new Storage();
        if (!getStep().isAppend()) {
            storage.setValue(getStep().getNewContent());
        } else {
            Page oldPage = requestOldPageContent(pageId);
            storage.setValue(
                    oldPage.getBody().getStorage().getValue()
                            + getStep().getNewContent()
            );
        }
        storage.setRepresentation(Storage.REPRESENTATION);
        return storage;
    }
}
