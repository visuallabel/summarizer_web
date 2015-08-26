/**
 * Copyright 2014 Tampere University of Technology, Pori Unit
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package otula.summarizerweb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;


/**
 * 
 * Simple thread-pool (queue) based task scheduler
 *
 */
public class Scheduler {
	private static final Logger LOGGER = Logger.getLogger(Scheduler.class);
	private static final int THREAD_POOL_SIZE = 1;	//change to more if MEAD can be made to work with multiple calls simultaneously
	private Semaphore _semaphore = new Semaphore(1);
	private ExecutorService _worker = null;

	/**
	 * Adds the given task to the execution queue
	 * 
	 * @param task
	 * @return true if successfully scheduled for execution
	 */
	public boolean executeTask(AbstractTask task){
		try {
			_semaphore.acquire();
			if(_worker == null){
				_worker = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			}
			_worker.execute(task);

			LOGGER.debug("Task started.");
		} catch (InterruptedException ex) {
			LOGGER.warn(ex, ex);
			return false;
		} finally {
			_semaphore.release();
		}
		return true;
	}

	/**
	 * Releases the resources reserved by the scheduler
	 * Note: this may or may not terminate the currently running task.
	 * 
	 * @return true if successfully shutdown
	 */
	public boolean shutdownWorkers(){
		try {
			_semaphore.acquire();
			if(_worker != null) {
				_worker.shutdown();
			}
		} catch (InterruptedException ex) {
			LOGGER.warn(ex, ex);
			return false;
		} finally {
			_worker = null;
			_semaphore.release();
		}
		return true;
	}
}
