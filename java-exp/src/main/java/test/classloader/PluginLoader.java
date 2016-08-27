/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.classloader;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import io.airlift.resolver.ArtifactResolver;
import io.airlift.resolver.DefaultArtifact;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.sonatype.aether.artifact.Artifact;


public class PluginLoader {
  private static final List<String> HIDDEN_CLASSES = ImmutableList.<String>builder().add("org.slf4j").build();

  private static final ImmutableList<String> PARENT_FIRST_CLASSES =
      ImmutableList.<String>builder().add("com.facebook.presto").add("com.fasterxml.jackson").add("io.airlift.slice")
          .add("javax.inject").add("javax.annotation").add("java.").build();

  private final ArtifactResolver resolver;

  public PluginLoader() {
    this.resolver = new ArtifactResolver(ArtifactResolver.USER_LOCAL_REPO, ArtifactResolver.MAVEN_CENTRAL_URI);
  }

  public void loadPlugin(String plugin) throws Exception {
    URLClassLoader pluginClassLoader = buildClassLoader(plugin);
    try (ThreadContextClassLoader ignored = new ThreadContextClassLoader(pluginClassLoader)) {
      // TODO
    }
  }

  private URLClassLoader buildClassLoader(String plugin) throws Exception {
    File file = new File(plugin);
    if (file.isFile() && (file.getName().equals("pom.xml") || file.getName().endsWith(".pom"))) {
      return buildClassLoaderFromPom(file);
    }
    if (file.isDirectory()) {
      return buildClassLoaderFromDirectory(file);
    }
    return buildClassLoaderFromCoordinates(plugin);
  }

  private URLClassLoader buildClassLoaderFromPom(File pomFile) throws Exception {
    List<Artifact> artifacts = resolver.resolvePom(pomFile);
    URLClassLoader classLoader = createClassLoader(artifacts, pomFile.getPath());

    Artifact artifact = artifacts.get(0);
//        Set<String> plugins = discoverPlugins(artifact, classLoader);
//        if (!plugins.isEmpty()) {
//            writePluginServices(plugins, artifact.getFile());
//        }

    return classLoader;
  }

  private URLClassLoader buildClassLoaderFromDirectory(File dir) throws Exception {
    List<URL> urls = new ArrayList<>();
    for (File file : listFiles(dir)) {
      urls.add(file.toURI().toURL());
    }
    return createClassLoader(urls);
  }

  private URLClassLoader buildClassLoaderFromCoordinates(String coordinates) throws Exception {
    Artifact rootArtifact = new DefaultArtifact(coordinates);
    List<Artifact> artifacts = resolver.resolveArtifacts(rootArtifact);
    return createClassLoader(artifacts, rootArtifact.toString());
  }

  private URLClassLoader createClassLoader(List<Artifact> artifacts, String name) throws IOException {
    List<URL> urls = new ArrayList<>();
    for (Artifact artifact : sortedArtifacts(artifacts)) {
      if (artifact.getFile() == null) {
        throw new RuntimeException("Could not resolve artifact: " + artifact);
      }
      File file = artifact.getFile().getCanonicalFile();
      urls.add(file.toURI().toURL());
    }
    return createClassLoader(urls);
  }

  private URLClassLoader createClassLoader(List<URL> urls) {
    ClassLoader parent = getClass().getClassLoader();
    return new PluginClassLoader(urls, parent, HIDDEN_CLASSES, PARENT_FIRST_CLASSES);
  }

  private static List<File> listFiles(File installedPluginsDir) {
    if (installedPluginsDir != null && installedPluginsDir.isDirectory()) {
      File[] files = installedPluginsDir.listFiles();
      if (files != null) {
        Arrays.sort(files);
        return ImmutableList.copyOf(files);
      }
    }
    return ImmutableList.of();
  }

  private static List<Artifact> sortedArtifacts(List<Artifact> artifacts) {
    List<Artifact> list = Lists.newArrayList(artifacts);
    Collections.sort(list, Ordering.natural().nullsLast().onResultOf(Artifact::getFile));
    System.out.println(list);
    return list;
  }
}
