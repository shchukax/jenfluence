package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.*;
import de.sprengnetter.jenkins.plugins.jenfluence.result.PageCreated;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.CreatePage;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.util.Collections;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 */
public class CreatePageExecution extends AbstractStepExecution<PageCreated, CreatePage> {

    private CreatePage createPage;

    public CreatePageExecution(final CreatePage createPage, final StepContext context, final ConfluenceSite confluenceSite) {
        super(createPage, context, confluenceSite);
        this.createPage = createPage;
    }

    @Override
    public void validate(final CreatePage step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type CreatePage is null");
        }

        if (step.getSite() == null) {
            throw new IllegalStateException("Given site is null");
        }

        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalStateException("The title of the page is null or emtpy");
        }

        if (step.getSpaceKey() == null || step.getSpaceKey().isEmpty()) {
            throw new IllegalStateException("The space of the new page is null or empty");
        }
    }

    @Override
    protected PageCreated run() throws Exception {
        try {
            return getService(ContentService.class).createPage(toPage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Page toPage() {
        Page page = new Page();
        page.setTitle(createPage.getTitle());
        page.setType("page");

        Space space = new Space();
        space.setKey(createPage.getSpaceKey());

        if (createPage.getParentId() != null && createPage.getParentId() != 0) {
            Ancestor ancestor = new Ancestor();
            ancestor.setId(createPage.getParentId());
            page.setAncestors(Collections.singletonList(ancestor));
        }

        page.setSpace(space);

        Body body = new Body();
        Storage storage = new Storage();
        storage.setValue(createPage.getContent());
        storage.setRepresentation("storage");
        body.setStorage(storage);

        page.setBody(body);

        return page;
    }
}
