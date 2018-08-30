package de.sprengnetter.jenkins.plugins.jenfluence.service;

import de.sprengnetter.jenkins.plugins.jenfluence.api.Content;
import de.sprengnetter.jenkins.plugins.jenfluence.api.Page;
import de.sprengnetter.jenkins.plugins.jenfluence.api.PageCreated;
import org.jboss.resteasy.plugins.providers.multipart.MultipartOutput;

public class ContentServiceImpl implements ContentService {

    @Override
    public Content getContent() {
        return null;
    }

    @Override
    public Content getContent(Integer limit) {
        return null;
    }

    @Override
    public Content getContent(String spaceKey) {
        return null;
    }

    @Override
    public Content getContent(String spaceKey, Integer limit) {
        return null;
    }

    @Override
    public Content getPage(String spaceKey, String title) {
        return null;
    }

    @Override
    public PageCreated createPage(Page page) {
        return null;
    }

    @Override
    public void attachFile(String id, MultipartOutput output) {

    }
}
