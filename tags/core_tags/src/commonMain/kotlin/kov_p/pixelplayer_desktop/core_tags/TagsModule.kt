package kov_p.pixelplayer_desktop.core_tags

import kov_p.pixelplayer_desktop.api_tags.TagsManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tagsModule = module {
    singleOf(::TagsManagerImpl).bind<TagsManager>()
}
