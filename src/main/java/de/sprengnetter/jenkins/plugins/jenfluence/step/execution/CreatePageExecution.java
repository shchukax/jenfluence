package de.sprengnetter.jenkins.plugins.jenfluence.step.execution;

import de.sprengnetter.jenkins.plugins.jenfluence.ConfluenceSite;
import de.sprengnetter.jenkins.plugins.jenfluence.api.*;
import de.sprengnetter.jenkins.plugins.jenfluence.service.ContentService;
import de.sprengnetter.jenkins.plugins.jenfluence.step.AbstractStepExecution;
import de.sprengnetter.jenkins.plugins.jenfluence.step.descriptor.CreatePageStep;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.util.Collections;

/**
 * @author Oliver Breitenbach
 * @version 1.0.0
 * Execution implementation of the step "createPage".
 */
public class CreatePageExecution extends AbstractStepExecution<PageCreated, CreatePageStep> {

    private static final long serialVersionUID = 7220386183041962984L;

    /**
     * Constructor that takes the needed information for the execution of the step.
     *
     * @param createPageStep The step that is going to be executed.
     * @param context        The step context.
     * @param confluenceSite The configured site of Confluence.
     */
    public CreatePageExecution(final CreatePageStep createPageStep, final StepContext context, final ConfluenceSite confluenceSite) {
        super(createPageStep, context, confluenceSite);
    }

    @Override
    public void validate(final CreatePageStep step) {
        if (isNull(step)) {
            throw new IllegalStateException("Given step of type CreatePageStep is null");
        }

        if (step.getSite() == null) {
            throw new IllegalStateException("Given site is null");
        }

        if (step.getTitle() == null || step.getTitle().isEmpty()) {
            throw new IllegalStateException("The title of the page is null or empty");
        }

        if (step.getSpaceKey() == null || step.getSpaceKey().isEmpty()) {
            throw new IllegalStateException("The space of the new page is null or empty");
        }
    }

    @Override
    protected PageCreated run() throws Exception {
        try {
            return getService(ContentService.class).createPage(buildNewPage());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Page buildNewPage() {
        Page page = new Page();
        page.setTitle(getStep().getTitle());
        page.setType("page");

        Space space = new Space();
        space.setKey(getStep().getSpaceKey());

        Ancestor ancestor = new Ancestor();

        switch (getStep().getParentPage().getValue().toLowerCase()) {
            case "title":
                ancestor.setId(getParentId());
                break;
            case "id":
                ancestor.setId(Integer.parseInt(getStep().getParentPage().getParentIdentifier()));
                break;
            default:
                ancestor = null;
                break;
        }

        if (ancestor == null) {
            page.setAncestors(null);
        } else {
            page.setAncestors(Collections.singletonList(ancestor));
        }

        page.setSpace(space);

        Body body = new Body();
        Storage storage = new Storage();
        storage.setValue(getStep().getContent());
        storage.setRepresentation("storage");
        body.setStorage(storage);

        page.setBody(body);
        return page;
    }

    private Integer getParentId() {
        ContentService service = getService(ContentService.class);
        Content content = service.getPage(getStep().getSpaceKey(), getStep().getParentPage().getParentIdentifier(), null);

        if (content.getResults().size() == 0 || content.getResults().get(0).getId() == null) {
            throw new IllegalStateException("No parent page with name " + getStep().getParentPage().getParentIdentifier() + " in space with key "
                    + getStep().getSpaceKey() + " was found");
        }

        /*
         * Should NEVER happen, because Confluence does not allow multiple pages with the same name in a space.
         * But you never know which bugs are present in Confluence or which new features are coming in future versions!
         * Better safe than sorry.
         */
        if (content.getResults().size() > 1) {
            throw new IllegalStateException("Multiple possible parent pages with the name " + getStep().getParentPage().getParentIdentifier()
                    + "in space with key " + getStep().getSpaceKey() + " were found");
        }
        return content.getResults().get(0).getId();
    }
}
